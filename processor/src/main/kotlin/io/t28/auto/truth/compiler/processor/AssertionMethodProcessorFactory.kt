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

package io.t28.auto.truth.compiler.processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.NoType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleElementVisitor8
import javax.lang.model.util.SimpleTypeVisitor8

typealias MethodProcessor = Processor<MethodSpec>

object AssertionMethodProcessorFactory {
    fun create(element: Element): List<MethodProcessor> {
        val property = element.accept(PropertyElementVisitor, Unit)
        return property.type.accept(PropertyTypeVisitor, property)
    }

    object PropertyElementVisitor : SimpleElementVisitor8<Property, Unit>() {
        override fun visitExecutable(element: ExecutableElement, parameter: Unit): Property {
            return Property.get(element)
        }

        override fun visitVariable(element: VariableElement, parameter: Unit): Property {
            return Property.get(element)
        }

        override fun defaultAction(element: Element, parameter: Unit): Property {
            throw IllegalArgumentException("Assertion method")
        }
    }

    object PropertyTypeVisitor : SimpleTypeVisitor8<List<MethodProcessor>, Property>() {
        private val BOXED_VOID = ClassName.get("java.lang", "Void")

        override fun visitPrimitive(type: PrimitiveType, property: Property): List<MethodProcessor> {
            if (type.kind == BOOLEAN) {
                val name = property.name
                val symbol = property.symbol
                return listOf(
                    BooleanIsMethodProcessor(name, symbol),
                    BooleanIsNotMethodProcessor(name, symbol)
                )
            }
            return defaultAction(type, property)
        }

        override fun visitDeclared(type: DeclaredType, property: Property): List<MethodProcessor> {
            val typeName = TypeName.get(type)
            if (typeName == BOXED_VOID) {
                return emptyList()
            }
            return defaultAction(type, property)
        }

        override fun visitNoType(type: NoType, property: Property): List<MethodProcessor> {
            return emptyList()
        }

        override fun defaultAction(type: TypeMirror, property: Property): List<MethodProcessor> {
            val name = property.name
            val symbol = property.symbol
            return listOf(
                ObjectHasMethodProcessor(type, name = name, symbol = symbol)
            )
        }
    }

    data class Property(
        val type: TypeMirror,
        val name: String,
        val symbol: String
    ) {
        companion object {
            private val GETTER_PREFIX = Regex("^(is|get)(.+?)$")

            fun get(element: ExecutableElement): Property {
                val name = "${element.simpleName}"
                return Property(
                    type = element.returnType,
                    name = name.simplify(),
                    symbol = "$name()"
                )
            }

            fun get(element: VariableElement): Property {
                val name = "${element.simpleName}"
                return Property(
                    type = element.asType(),
                    name = name.simplify(),
                    symbol = name
                )
            }

            private fun String.simplify(): String {
                val matched = GETTER_PREFIX.find(this) ?: return this
                val candidate = matched.groups[2]?.value ?: return this
                if (candidate.first() !in 'A'..'Z') {
                    return this
                }
                return candidate.decapitalize()
            }
        }
    }
}
