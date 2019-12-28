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
import io.t28.auto.truth.compiler.Context
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleElementVisitor8
import javax.lang.model.util.SimpleTypeVisitor8

abstract class PropertyProcessor<E : Element> internal constructor(
    private val context: Context,
    private val element: E
) : Processor<List<MethodSpec>> {
    companion object {
        private val GETTER_PREFIX = Regex("^(is|get)(.+?)$")
        private val BOXED_VOID = ClassName.get("java.lang", "Void")
        private val BOXED_BOOLEAN = ClassName.get("java.lang", "Boolean")

        fun create(context: Context, element: Element): PropertyProcessor<out Element> {
            return element.accept(PropertyProcessorFactory, context)
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

    protected abstract val type: TypeMirror

    protected abstract val name: String

    protected abstract val symbol: String

    override fun process(): List<MethodSpec> {
        return type.accept(object : SimpleTypeVisitor8<List<MethodSpec>, Unit>() {
            override fun visitPrimitive(type: PrimitiveType, parameter: Unit) = process(type)

            override fun visitArray(type: ArrayType, parameter: Unit) = process(type)

            override fun visitDeclared(type: DeclaredType, parameter: Unit) = process(type)

            override fun defaultAction(type: TypeMirror, parameter: Unit) = process(type)
        }, Unit)
    }

    private fun process(type: PrimitiveType): List<MethodSpec> {
        return when (type.kind) {
            BOOLEAN -> {
                listOf(
                    BooleanIsMethodProcessor(name = name, symbol = symbol),
                    BooleanIsNotMethodProcessor(name = name, symbol = symbol)
                ).map(Processor<MethodSpec>::process)
            }
            else -> {
                listOf(
                    ObjectHasMethodProcessor(type, name = name, symbol = symbol)
                ).map(Processor<MethodSpec>::process)
            }
        }
    }

    private fun process(type: ArrayType): List<MethodSpec> {
        return listOf(
            ArrayTypePropertyProcessor(type, name = name, symbol = symbol)
        ).map(Processor<MethodSpec>::process)
    }

    private fun process(type: DeclaredType): List<MethodSpec> {
        return when (TypeName.get(type)) {
            BOXED_VOID -> {
                emptyList()
            }
            BOXED_BOOLEAN -> {
                listOf(
                    BooleanIsMethodProcessor(name = name, symbol = symbol),
                    BooleanIsNotMethodProcessor(name = name, symbol = symbol)
                ).map(Processor<MethodSpec>::process)
            }
            else -> {
                listOf(
                    ObjectHasMethodProcessor(type, name = name, symbol = symbol)
                ).map(Processor<MethodSpec>::process)
            }
        }
    }

    private fun process(type: TypeMirror): List<MethodSpec> {
        context.logger.debug(element, "Unsupported property type: %s", type)
        return emptyList()
    }

    private object PropertyProcessorFactory : SimpleElementVisitor8<PropertyProcessor<out Element>, Context>() {
        override fun visitExecutable(element: ExecutableElement, context: Context): PropertyProcessor<out Element> {
            val simpleName = "${element.simpleName}"
            return object : PropertyProcessor<ExecutableElement>(context, element) {
                override val type: TypeMirror
                    get() = element.returnType

                override val name: String
                    get() = simpleName.simplify()

                override val symbol: String
                    get() = "$simpleName()"
            }
        }

        override fun visitVariable(element: VariableElement, context: Context): PropertyProcessor<out Element> {
            val simpleName = "${element.simpleName}"
            return object : PropertyProcessor<VariableElement>(context, element) {
                override val type: TypeMirror
                    get() = element.asType()

                override val name: String
                    get() = simpleName.simplify()

                override val symbol: String
                    get() = simpleName
            }
        }

        override fun defaultAction(element: Element, context: Context): PropertyProcessor<out Element> {
            context.logger.error(element, "Unsupported element: kind=%s", element.kind)
            throw IllegalArgumentException("Unsupported kind of element: ${element.kind}")
        }
    }
}
