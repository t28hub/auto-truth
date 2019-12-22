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

package io.t28.auto.truth.compiler

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import javax.lang.model.element.Modifier

class SubjectClass(private val element: TypeElementWrapper) : ClassDeclaration {
    override val packageName: String
        get() = element.packageName

    private val normalizedName: String
        get() = element.name.replace('$', '_')

    private val className: ClassName
        get() = ClassName.get(packageName, "Auto_${normalizedName}Subject")

    private val type: TypeName
        get() = element.type.asTypeName()

    override fun toSpec(): TypeSpec {
        val builder = TypeSpec.classBuilder(className)
        builder.apply {
            superclass(Subject::class.java)
            addModifiers(Modifier.PUBLIC)
            addTypeVariable(TypeVariableName.get("T", type))
            addField(createActualField())
            addMethod(createConstructor())
            addMethod(createFactoryMethod())
            addMethods(createFieldAssertionMethods())
            addMethods(createMethodAssertionMethods())
        }
        return builder.build()
    }

    private fun createActualField(): FieldSpec {
        val type = element.type
        return FieldSpec.builder(type.asTypeName(), "actual")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build()
    }

    private fun createConstructor(): MethodSpec {
        val builder = MethodSpec.constructorBuilder()
        builder.apply {
            addModifiers(Modifier.PRIVATE)
            addParameter(ParameterSpec.builder(FailureMetadata::class.java, "failureMetadata").build())
            addParameter(ParameterSpec.builder(type, "actual").build())
            addCode(CodeBlock.builder().addStatement("super(\$L, \$L)", "failureMetadata", "actual").build())
            addCode(CodeBlock.builder().addStatement("this.\$L = \$L", "actual", "actual").build())
        }
        return builder.build()
    }

    private fun createFactoryMethod(): MethodSpec {
        val builder = MethodSpec.methodBuilder(element.name.decapitalize())
        builder.apply {
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            returns(ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), className, type))
            addCode(CodeBlock.builder().addStatement("return \$T::new", className).build())
        }
        return builder.build()
    }

    private fun createFieldAssertionMethods(): Collection<MethodSpec> {
        return element.findFields { field -> !field.isStatic }
                .filterNot { field -> field.isPrivate or field.isProtected }
                .filterNot { field ->
                    val fieldType = field.type
                    fieldType.isVoid or fieldType.isClassOf<Void>()
                }
                .map { field ->
                    val name = field.name
                    MethodSpec.methodBuilder("has${name.capitalize()}")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(field.type.asTypeName(), "expected")
                            .addCode(CodeBlock.builder().addStatement("check(\$S).that(this.\$L.\$L).isEqualTo(\$L)", name, "actual", name, "expected").build())
                            .build()
                }
    }

    private fun createMethodAssertionMethods(): Collection<MethodSpec> {
        return element.findMethods { method -> !method.hasParameter }
                .filterNot { method -> method.isStatic }
                .filterNot { method -> method.isPrivate or method.isProtected }
                .filterNot { method ->
                    val returnType = method.returnType
                    returnType.isVoid or returnType.isClassOf<Void>()
                }
                .map { method ->
                    val name = method.name
                    MethodSpec.methodBuilder("has${name.capitalize()}")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(method.returnType.asTypeName(), "expected")
                            .addCode(CodeBlock.builder().addStatement("check(\$S).that(this.\$L.\$L()).isEqualTo(\$L)", "${name}()", "actual", name, "expected").build())
                            .build()
                }
    }
}
