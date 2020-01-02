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

package io.t28.auto.truth.processor.generator

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.t28.auto.truth.processor.AutoTruthProcessor
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.generator.method.MethodGenerator
import javax.annotation.Generated
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

class SubjectClassGenerator(
    private val methodGenerators: List<MethodGenerator>
) : Generator<SubjectClass, TypeSpec> {
    constructor(vararg generators: MethodGenerator) : this(generators.toList())

    override fun generate(input: SubjectClass): TypeSpec {
        val element = input.element
        val type = TypeName.get(input.type)
        val className = ClassName.get(input.packageName, "${input.prefix}${element.simpleName}${input.suffix}")
        return TypeSpec.classBuilder(className).apply {
            // Annotations
            addAnnotation(AnnotationSpec.builder(Generated::class.java).apply {
                addMember("value", "\$S", AutoTruthProcessor::class.java.canonicalName)
            }.build())
            addAnnotation(AnnotationSpec.builder(SuppressWarnings::class.java).apply {
                addMember("value", "\$S", "unchecked")
            }.build())

            superclass(Subject::class.java)
            addModifiers(PUBLIC)

            // Fields
            addField(FieldSpec.builder(type, "actual", PRIVATE, FINAL).build())

            // Constructors
            addMethod(MethodSpec.constructorBuilder().apply {
                addParameter(ParameterSpec.builder(FailureMetadata::class.javaObjectType, "failureMetadata").build())
                addParameter(ParameterSpec.builder(type, "actual").build())
                addStatement("super(\$L, \$L)", "failureMetadata", "actual")
                addStatement("this.\$L = \$L", "actual", "actual")
            }.build())

            // Factory method
            addMethod(MethodSpec.methodBuilder(input.name.decapitalize()).apply {
                returns(ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), className, type))
                addModifiers(PUBLIC, STATIC)
                addStatement("return \$T::new", className)
            }.build())

            // Assertion methods
            val methods = input.properties.flatMap { property ->
                methodGenerators.filter { generator -> generator.matches(property.type) }
                    .map { generator -> generator.generate(property) }
            }
            addMethods(methods)
        }.build()
    }
}
