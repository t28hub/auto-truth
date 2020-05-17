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

package io.t28.auto.truth.processor.generator.method

import com.google.common.truth.Fact
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.Modifier
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

sealed class BooleanAssertionGenerator(protected val context: Context) : MethodGenerator {
    final override fun matches(type: TypeMirror): Boolean {
        return object : SupportedTypeMatcher<Void?>() {
            override fun visitPrimitive(type: PrimitiveType, p: Void?): Boolean {
                return type.kind == TypeKind.BOOLEAN
            }

            override fun visitDeclared(type: DeclaredType, p: Void?): Boolean {
                val boxedBooleanType = context.utils.getDeclaredType(java.lang.Boolean::class)
                return context.utils.isAssignableType(type, boxedBooleanType)
            }
        }.visit(type)
    }

    final override fun generate(input: Property): MethodSpec {
        require(matches(input.type))
        context.logger.debug(input.element, "Generating an assertion method for boolean and Boolean")
        return MethodSpec.methodBuilder(generateName(input)).apply {
            addModifiers(Modifier.PUBLIC)
            addCode(generateCode(input))
        }.build()
    }

    protected abstract fun generateName(input: Property): String

    protected abstract fun generateCode(input: Property): CodeBlock

    class PositiveAssertionGenerator(context: Context) : BooleanAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "is${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            return CodeBlock.builder().apply {
                beginControlFlow("if (!this.\$L.\$L)", "actual", input.symbol).apply {
                    addStatement("failWithActual(\$T.simpleFact(\$S))", Fact::class.java, "expected to be ${input.name}")
                }
                endControlFlow()
            }.build()
        }
    }

    class NegativeAssertionGenerator(context: Context) : BooleanAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "isNot${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            return CodeBlock.builder().apply {
                beginControlFlow("if (this.\$L.\$L)", "actual", input.symbol).apply {
                    addStatement("failWithActual(\$T.simpleFact(\$S))", Fact::class.java, "expected not to be ${input.name}")
                }
                endControlFlow()
            }.build()
        }
    }
}
