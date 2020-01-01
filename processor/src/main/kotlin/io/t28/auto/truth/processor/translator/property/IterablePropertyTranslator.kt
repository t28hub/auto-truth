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

package io.t28.auto.truth.processor.translator.property

import com.google.common.truth.IterableSubject
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.translator.PropertyTranslator
import java.util.Arrays
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleTypeVisitor8

class IterablePropertyTranslator(private val context: Context) : PropertyTranslator {
    override fun matches(type: TypeMirror): Boolean {
        return type.accept(object : SimpleTypeVisitor8<Boolean, Unit>() {
            override fun visitDeclared(type: DeclaredType, parameter: Unit): Boolean {
                // Iterable<*>
                val iterable = context.erasureType(context.getTypeElement(Iterable::class).asType())
                return context.isInherited(type, iterable)
            }

            override fun defaultAction(type: TypeMirror, parameter: Unit) = false
        }, Unit)
    }

    override fun translate(input: Property): MethodSpec {
        val symbol = input.symbol
        return MethodSpec.methodBuilder(input.name.decapitalize()).apply {
            returns(IterableSubject::class.java)
            addModifiers(PUBLIC)
            addStatement("return check(\$S).that(\$L.\$L)", symbol, "actual", symbol)
        }.build()
    }

    class PositiveTranslator(private val context: Context) : PropertyTranslator {
        override fun matches(type: TypeMirror): Boolean {
            return type.accept(object : SimpleTypeVisitor8<Boolean, Unit>() {
                override fun visitDeclared(type: DeclaredType, parameter: Unit): Boolean {
                    // Iterable<*>
                    val iterable = context.erasureType(context.getTypeElement(Iterable::class).asType())
                    if (!context.isInherited(type, iterable)) {
                        return false
                    }

                    val typeArguments = type.typeArguments
                    return typeArguments.size == 1
                }

                override fun defaultAction(type: TypeMirror, parameter: Unit) = false
            }, Unit)
        }

        override fun translate(input: Property): MethodSpec {
            require(matches(input.type))

            val symbol = input.symbol
            val type = input.type as DeclaredType
            val componentType = type.typeArguments.first()
            val parameterType = context.getArrayType(componentType)
            return MethodSpec.methodBuilder("has${input.name.capitalize()}").apply {
                addModifiers(PUBLIC)
                addParameter(ParameterSpec.builder(TypeName.get(parameterType), "expected").build())
                varargs(true)
                addStatement(
                    "check(\$S).that(\$L.\$L).containsAtLeastElementsIn(\$T.asList(\$L))",
                    symbol, "actual", symbol, Arrays::class.java, "expected"
                )
            }.build()
        }
    }

    class NegativeTranslator(private val context: Context) : PropertyTranslator {
        override fun matches(type: TypeMirror): Boolean {
            return type.accept(object : SimpleTypeVisitor8<Boolean, Unit>() {
                override fun visitDeclared(type: DeclaredType, parameter: Unit): Boolean {
                    // Iterable<*>
                    val iterable = context.erasureType(context.getTypeElement(Iterable::class).asType())
                    if (!context.isInherited(type, iterable)) {
                        return false
                    }

                    val typeArguments = type.typeArguments
                    return typeArguments.size == 1
                }

                override fun defaultAction(type: TypeMirror, parameter: Unit) = false
            }, Unit)
        }

        override fun translate(input: Property): MethodSpec {
            require(matches(input.type))

            val symbol = input.symbol
            val type = input.type as DeclaredType
            val componentType = type.typeArguments.first()
            val parameterType = context.getArrayType(componentType)
            return MethodSpec.methodBuilder("doesNotHave${input.name.capitalize()}").apply {
                addModifiers(PUBLIC)
                addParameter(ParameterSpec.builder(TypeName.get(parameterType), "expected").build())
                varargs(true)
                addStatement(
                    "check(\$S).that(\$L.\$L).containsNoneIn(\$T.asList(\$L))",
                    symbol, "actual", symbol, Arrays::class.java, "expected"
                )
            }.build()
        }
    }
}
