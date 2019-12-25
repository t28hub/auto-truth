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
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.t28.auto.truth.compiler.dsl.`class`
import io.t28.auto.truth.compiler.dsl.extends
import io.t28.auto.truth.compiler.dsl.param
import javax.annotation.Generated
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

@Suppress("StringLiteralDuplication")
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
        return `class`(className) {
            this extends Subject::class

            `@`(Generated::class) {
                "value" `is` AutoTruthProcessor::class.java.canonicalName
            }

            `@`(SuppressWarnings::class) {
                "value" `is` "unchecked"
            }

            typeParams("T" extends type)

            modifiers(PUBLIC)

            field(type, "actual") {
                modifiers(PRIVATE, FINAL)
            }

            constructor(
                    param(FailureMetadata::class, "failureMetadata"),
                    param(type, "actual")
            ) {
                modifiers(PRIVATE)
                statement("super(\$L, \$L)", "failureMetadata", "actual")
                statement("this.\$L = \$L", "actual", "actual")
            }

            // Factory method
            method(element.name.decapitalize()) {
                modifiers(PUBLIC, STATIC)
                statement("return \$T::new", className)
                this returns ParameterizedTypeName.get(ClassName.get(Subject.Factory::class.java), className, type)
            }

            findPropertyFields().map { field ->
                val name = field.name
                val type = field.type
                method(
                        "has${name.capitalize()}",
                        param(type.asTypeName(), "expected")
                ) {
                    modifiers(PUBLIC)
                    statement("check(\$S).that(this.\$L.\$L).isEqualTo(\$L)", name, "actual", name, "expected")
                }
            }

            findGetterMethods().map { method ->
                val name = method.name
                val type = method.returnType
                method(
                        "has${name.capitalize()}",
                        param(type.asTypeName(), "expected")
                ) {
                    modifiers(PUBLIC)
                    statement("check(\$S).that(this.\$L.\$L()).isEqualTo(\$L)", "$name()", "actual", name, "expected")
                }
            }
        }
    }

    private fun findPropertyFields(): Collection<VariableElementWrapper> {
        return element.findFields { field -> !field.isStatic }
                .filterNot { field -> field.isPrivate or field.isProtected }
                .filterNot { field ->
                    val fieldType = field.type
                    fieldType.isVoid or fieldType.isClassOf<Void>()
                }
    }

    private fun findGetterMethods(): Collection<ExecutableElementWrapper> {
        return element.findMethods { method -> !method.hasParameter }
                .filterNot { method -> method.isStatic }
                .filterNot { method -> method.isPrivate or method.isProtected }
                .filterNot { method ->
                    val returnType = method.returnType
                    returnType.isVoid or returnType.isClassOf<Void>()
                }
    }
}
