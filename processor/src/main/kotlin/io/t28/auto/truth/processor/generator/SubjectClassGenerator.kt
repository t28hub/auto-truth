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
import com.google.common.truth.Truth
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import io.t28.auto.truth.processor.AutoTruthProcessor
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.data.ValueObjectClass
import io.t28.auto.truth.processor.generator.method.MethodGenerator
import javax.annotation.Generated
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PROTECTED
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

class SubjectClassGenerator(
    private val methodGenerators: List<MethodGenerator>
) : Generator<SubjectClass, TypeSpec> {
    constructor(vararg generators: MethodGenerator) : this(generators.toList())

    override fun generate(input: SubjectClass): TypeSpec {
        val className = ClassName.get(input.packageName, input.simpleName)
        return TypeSpec.classBuilder(className).apply {
            addAnnotations(generateAnnotations())
            addTypeVariables(input.valueObject.typeVariables())
            superclass(Subject::class.java)
            addModifiers(PUBLIC)
            addFields(generateFields(input))
            addMethod(generateConstructor(input))
            addMethod(generateAssertThat(input))
            addMethods(generateAssertions(input))
        }.build()
    }

    private fun generateAnnotations(): List<AnnotationSpec> {
        return listOf(
            AnnotationSpec.builder(Generated::class.java)
                .addMember("value", "\$S", AutoTruthProcessor::class.java.canonicalName)
                .build(),
            AnnotationSpec.builder(SuppressWarnings::class.java)
                .addMember("value", "\$S", "unchecked")
                .build()
        )
    }

    private fun generateFields(input: SubjectClass): List<FieldSpec> {
        return listOf(
            FieldSpec.builder(TypeName.get(input.valueObject.type), "actual", PRIVATE, FINAL).build()
        )
    }

    private fun generateConstructor(input: SubjectClass): MethodSpec {
        return MethodSpec.constructorBuilder().apply {
            addModifiers(PROTECTED)
            addParameter(ParameterSpec.builder(FailureMetadata::class.javaObjectType, "failureMetadata").apply {
                addAnnotation(Nonnull::class.java)
            }.build())
            addParameter(ParameterSpec.builder(TypeName.get(input.valueObject.type), "actual").apply {
                addAnnotation(Nullable::class.java)
            }.build())
            addStatement("super(\$L, \$L)", "failureMetadata", "actual")
            addStatement("this.\$L = \$L", "actual", "actual")
        }.build()
    }

    private fun generateAssertThat(input: SubjectClass): MethodSpec {
        val valueObject = input.valueObject
        val valueObjectName = TypeName.get(valueObject.type)
        val subjectClassName = valueObject.typeVariables().let { variables ->
            if (variables.isEmpty()) {
                ClassName.get(input.packageName, input.simpleName)
            } else {
                ParameterizedTypeName.get(ClassName.get(input.packageName, input.simpleName), *variables.toTypedArray())
            }
        }

        return MethodSpec.methodBuilder("assertThat").apply {
            returns(subjectClassName)
            addTypeVariables(valueObject.typeVariables())
            addModifiers(PUBLIC, STATIC)
            addAnnotation(Nonnull::class.java)
            addParameter(ParameterSpec.builder(valueObjectName, "actual").apply {
                addAnnotation(Nullable::class.java)
            }.build())
            addStatement("return \$T.assertAbout(\$L).that(\$L)",
                Truth::class.java, generateFactory(subjectClass = subjectClassName, valueObject = valueObjectName), "actual")
        }.build()
    }

    private fun generateFactory(subjectClass: TypeName, valueObject: TypeName): TypeSpec {
        return TypeSpec.anonymousClassBuilder("").apply {
            addSuperinterface(ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), subjectClass, valueObject))
            addMethod(MethodSpec.methodBuilder("createSubject").apply {
                addAnnotation(Override::class.java)
                addModifiers(PUBLIC, FINAL)
                returns(subjectClass)
                addParameter(FailureMetadata::class.java, "metadata")
                addParameter(valueObject, "_actual")
                addStatement("return new \$T(metadata, _actual)", subjectClass)
            }.build())
        }.build()
    }

    private fun generateAssertions(input: SubjectClass): List<MethodSpec> {
        val valueObject = input.valueObject
        return (valueObject.findProperties() + valueObject.findEnumConstants()).flatMap { property ->
            methodGenerators.filter { generator -> generator.isSupported(property) }
                .map { generator -> generator.generate(property) }
        }
    }

    private fun ValueObjectClass.typeVariables(): List<TypeVariableName> {
        return element.typeParameters.map { typeParameter ->
            TypeVariableName.get(typeParameter)
        }
    }
}
