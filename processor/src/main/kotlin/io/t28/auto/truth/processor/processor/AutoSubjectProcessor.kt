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

import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.extensions.asString
import io.t28.auto.truth.processor.extensions.asType
import io.t28.auto.truth.processor.extensions.asTypeElement
import io.t28.auto.truth.processor.extensions.findAnnotationMirror
import io.t28.auto.truth.processor.extensions.findFields
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.extensions.getAnnotationValue
import io.t28.auto.truth.processor.extensions.getPackage
import io.t28.auto.truth.processor.extensions.hasParameter
import io.t28.auto.truth.processor.extensions.isPublic
import io.t28.auto.truth.processor.extensions.isStatic
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

class AutoSubjectProcessor(
    context: Context,
    private val variablePropertyProcessor: VariablePropertyProcessor,
    private val executablePropertyProcessor: ExecutablePropertyProcessor
) : Processor<TypeElement, SubjectClass> {
    private val logger = context.logger

    override fun process(element: TypeElement): SubjectClass {
        val annotation = element.findAnnotationMirror<AutoSubject>() ?: run {
            logger.error(element, "Required annotation %s is missing", AutoSubject::class.simpleName)
            throw IllegalStateException("Required annotation ${AutoSubject::class.simpleName} does not exist ${element.qualifiedName}")
        }

        // TODO: Check prefix and suffix whether the value is valid Java identifier or not
        val packageName = "${element.getPackage().qualifiedName}"
        val value = requireNotNull(annotation.getAnnotationValue("value")).asType()
        val classPrefix = requireNotNull(annotation.getAnnotationValue("prefix")).asString()
        val classSuffix = requireNotNull(annotation.getAnnotationValue("suffix")).asString()
        return SubjectClass(
            packageName = packageName,
            prefix = classPrefix,
            suffix = classSuffix,
            element = (value as DeclaredType).asTypeElement(),
            type = value,
            name = "${element.simpleName}",
            properties = element.findProperties()
        )
    }

    private fun TypeElement.findProperties(): List<Property> {
        // Find public and non-static properties
        val fieldProperties = findFields {
            it.isPublic and !it.isStatic
        }.map { variablePropertyProcessor.process(it) }

        // Find public and non-static getter methods
        val getterProperties = findMethods {
            it.isPublic and !it.isStatic and !it.hasParameter
        }.map { executablePropertyProcessor.process(it) }
        return fieldProperties + getterProperties
    }
}
