/*
 * Copyright 2019 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.auto.truth.processor.processor

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.AutoTruthProcessor
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.dsl.`class`
import io.t28.auto.truth.processor.dsl.extends
import io.t28.auto.truth.processor.dsl.param
import io.t28.auto.truth.processor.extensions.asString
import io.t28.auto.truth.processor.extensions.findAnnotationMirror
import io.t28.auto.truth.processor.extensions.findFields
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.extensions.getAnnotationValue
import io.t28.auto.truth.processor.extensions.getPackage
import io.t28.auto.truth.processor.extensions.hasParameter
import io.t28.auto.truth.processor.extensions.isPublic
import io.t28.auto.truth.processor.extensions.isStatic
import javax.annotation.Generated
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

class AutoSubjectProcessor(
    private val context: Context,
    private val element: TypeElement
) : Processor<TypeSpec> {
    val packageName: String
        get() = "${element.getPackage().qualifiedName}"

    private val logger = context.logger

    override fun process(): TypeSpec {
        val annotation = element.findAnnotationMirror<AutoSubject>() ?: run {
            logger.error(element, "Required annotation %s is missing", AutoSubject::class.simpleName)
            throw IllegalStateException("Required annotation ${AutoSubject::class.simpleName} does not exist ${element.qualifiedName}")
        }

        // TODO: Check prefix and suffix whether the value is valid Java identifier or not
        val classPrefix = requireNotNull(annotation.getAnnotationValue("prefix")?.asString())
        val classSuffix = requireNotNull(annotation.getAnnotationValue("suffix")?.asString())
        val className = ClassName.get(packageName, "$classPrefix${element.simpleName}$classSuffix")
        val simpleName = "${element.simpleName}"

        val type = TypeName.get(element.asType())
        return `class`(className) {
            `@`(Generated::class) {
                "value" `is` AutoTruthProcessor::class.java.canonicalName
            }

            `@`(SuppressWarnings::class) {
                "value" `is` "unchecked"
            }

            this extends Subject::class
            modifiers(PUBLIC)

            field(type, "actual") {
                modifiers(PRIVATE, FINAL)
            }

            constructor(
                param(FailureMetadata::class, "failureMetadata"),
                param(type, "actual")
            ) {
                statement("super(\$L, \$L)", "failureMetadata", "actual")
                statement("this.\$L = \$L", "actual", "actual")
            }

            // Factory method
            method(simpleName.decapitalize()) {
                modifiers(PUBLIC, STATIC)
                statement("return \$T::new", className)
                this returns ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), className, type)
            }

            // Assertion methods
            assertionMethods().forEach {
                method(it)
            }
        }
    }

    private fun assertionMethods(): List<MethodSpec> {
        return findProperties()
            .map { PropertyProcessor.create(context, it) }
            .flatMap(PropertyProcessor<out Element>::process)
    }

    private fun findProperties(): List<Element> {
        // Find public and non-static properties
        val fieldProperties = element.findFields {
            it.isPublic and !it.isStatic
        }

        // Find public and non-static getter methods
        val getterProperties = element.findMethods {
            it.isPublic and !it.isStatic and !it.hasParameter
        }
        return fieldProperties + getterProperties
    }
}
