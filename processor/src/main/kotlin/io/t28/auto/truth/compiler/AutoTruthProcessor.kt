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

import com.google.auto.service.AutoService
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.compiler.extensions.getAnnotatedElements
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class AutoTruthProcessor : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AutoSubject::class.java.canonicalName)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            return true
        }

        roundEnv.getAnnotatedElements<AutoSubject>()
                .filterIsInstance<TypeElement>()
                .forEach { element ->
                    val type = ElementWrapper.wrap(element)
                    val normalizedValueClassName = type.name.replace('$', '_')
                    val className = ClassName.get(type.packageName, "Auto_${normalizedValueClassName}Subject")
                    val typeBuilder = TypeSpec.classBuilder(className)
                            .superclass(Subject::class.java)
                            .addModifiers(Modifier.PUBLIC)
                            .addTypeVariable(TypeVariableName.get("T", TypeName.get(element.asType())))

                    val actualField = FieldSpec.builder(TypeName.get(element.asType()), "actual")
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                            .build()
                    typeBuilder.addField(actualField)

                    val factoryMethod = MethodSpec.methodBuilder(type.name.decapitalize())
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .returns(ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), className, TypeName.get(element.asType())))
                            .addCode(CodeBlock.builder().addStatement("return \$T::new", className).build())
                            .build()
                    typeBuilder.addMethod(factoryMethod)

                    val constructor = MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .addParameter(ParameterSpec.builder(FailureMetadata::class.java, "failureMetadata").build())
                            .addParameter(ParameterSpec.builder(TypeName.get(element.asType()), "actual").build())
                            .addCode(CodeBlock.builder().addStatement("super(\$L, \$L)", "failureMetadata", "actual").build())
                            .addCode(CodeBlock.builder().addStatement("this.\$L = \$L", "actual", "actual").build())
                            .build()
                    typeBuilder.addMethod(constructor)

                    val fieldAccessors = type.findFields { field -> !field.isStatic }
                            .filterNot { field -> field.isPrivate or field.isProtected }
                            .filterNot { field ->
                                val fieldType = TypeName.get(field.type)
                                return@filterNot (fieldType == TypeName.VOID) or (fieldType == TypeName.get(Void::class.java))
                            }
                            .map { field ->
                                val name = field.name
                                MethodSpec.methodBuilder("has${name.capitalize()}")
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(TypeName.get(field.type), "expected")
                                        .addCode(CodeBlock.builder().addStatement("check(\$S).that(this.\$L.\$L).isEqualTo(\$L)", name, "actual", name, "expected").build())
                                        .build()
                            }
                    typeBuilder.addMethods(fieldAccessors)

                    val methodAccessors = type.findMethods { method -> !method.hasParameter }
                            .filterNot { method -> method.isStatic }
                            .filterNot { method -> method.isPrivate or method.isProtected }
                            .filterNot { method ->
                                val returnType = TypeName.get(method.returnType)
                                return@filterNot (returnType == TypeName.VOID) or (returnType == TypeName.get(Void::class.java))
                            }
                            .map { method ->
                                val name = method.name
                                MethodSpec.methodBuilder("has${name.capitalize()}")
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(TypeName.get(method.returnType), "expected")
                                        .addCode(CodeBlock.builder().addStatement("check(\$S).that(this.\$L.\$L()).isEqualTo(\$L)", "${name}()", "actual", name, "expected").build())
                                        .build()
                            }
                    typeBuilder.addMethods(methodAccessors)

                    val javaFile = JavaFile.builder(className.packageName(), typeBuilder.build())
                            .indent("    ")
                            .skipJavaLangImports(true)
                            .build()
                    javaFile.writeTo(processingEnv.filer)
                }
        return true
    }
}
