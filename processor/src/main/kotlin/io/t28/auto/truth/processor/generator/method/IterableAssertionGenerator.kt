/*
 * Copyright 2020 Tatsuya Maki
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

package io.t28.auto.truth.processor.generator.method

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import java.util.Arrays
import javax.lang.model.element.Modifier
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

sealed class IterableAssertionGenerator(protected val context: Context) : MethodGenerator {
    override fun matches(type: TypeMirror): Boolean {
        return object : SupportedTypeMatcher<Void?>() {
            override fun visitDeclared(type: DeclaredType, p: Void?): Boolean {
                val iterableType = context.utils.getDeclaredType(Iterable::class)
                if (!context.utils.isAssignableType(type, iterableType)) {
                    return false
                }
                return type.typeArguments.size == 1
            }
        }.visit(type)
    }

    final override fun generate(input: Property): MethodSpec {
        require(matches(input.type))
        context.logger.debug(input.element, "Generating an assertion method for Iterable<T>")

        val type = input.type as DeclaredType
        val componentType = type.typeArguments.first()
        val parameterType = context.utils.getArrayType(componentType)
        return MethodSpec.methodBuilder(generateName(input)).apply {
            addModifiers(Modifier.PUBLIC)
            addParameter(ParameterSpec.builder(TypeName.get(parameterType), "expected").build())
            varargs(true)
            addCode(generateCode(input))
        }.build()
    }

    protected abstract fun generateName(input: Property): String

    protected abstract fun generateCode(input: Property): CodeBlock

    class PositiveAssertionGenerator(context: Context) : IterableAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "has${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            val symbol = input.symbol
            return CodeBlock.builder().apply {
                addStatement(
                    "check(\$S).that(\$L.\$L).containsAtLeastElementsIn(\$T.asList(\$L))",
                    symbol, "actual", symbol, Arrays::class.java, "expected"
                )
            }.build()
        }
    }

    class NegativeAssertionGenerator(context: Context) : IterableAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "doesNotHave${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            val symbol = input.symbol
            return CodeBlock.builder().apply {
                addStatement(
                    "check(\$S).that(\$L.\$L).containsNoneIn(\$T.asList(\$L))",
                    symbol, "actual", symbol, Arrays::class.java, "expected"
                )
            }.build()
        }
    }
}
