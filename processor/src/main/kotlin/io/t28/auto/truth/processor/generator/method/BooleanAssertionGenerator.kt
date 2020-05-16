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

class BooleanAssertionGenerator internal constructor(
    private val context: Context,
    private val nameFactory: (Property) -> String,
    private val codeFactory: (Property) -> CodeBlock
) : MethodGenerator {
    override fun matches(type: TypeMirror): Boolean {
        return type.accept(BooleanTypeMatcher, context)
    }

    override fun generate(input: Property): MethodSpec {
        require(matches(input.type))
        context.logger.debug(input.element, "Generating an assertion method for boolean and Boolean")
        return MethodSpec.methodBuilder(nameFactory(input)).apply {
            addModifiers(Modifier.PUBLIC)
            addCode(codeFactory(input))
        }.build()
    }

    companion object {
        fun positiveAssertion(context: Context): BooleanAssertionGenerator {
            return BooleanAssertionGenerator(
                context,
                { "is${it.name.capitalize()}" },
                {
                    CodeBlock.builder().apply {
                        beginControlFlow("if (!this.\$L.\$L)", "actual", it.symbol).apply {
                            addStatement("failWithActual(\$T.simpleFact(\$S))", Fact::class.java, "expected to be ${it.name}")
                        }
                        endControlFlow()
                    }.build()
                }
            )
        }

        fun negativeAssertion(context: Context): BooleanAssertionGenerator {
            return BooleanAssertionGenerator(
                context,
                { "isNot${it.name.capitalize()}" },
                {
                    CodeBlock.builder().apply {
                        beginControlFlow("if (this.\$L.\$L)", "actual", it.symbol).apply {
                            addStatement("failWithActual(\$T.simpleFact(\$S))", Fact::class.java, "expected not to be ${it.name}")
                        }
                        endControlFlow()
                    }.build()
                }
            )
        }
    }

    internal object BooleanTypeMatcher : SupportedTypeMatcher() {
        override fun visitPrimitive(type: PrimitiveType, context: Context): Boolean {
            return type.kind == TypeKind.BOOLEAN
        }

        override fun visitDeclared(type: DeclaredType, context: Context): Boolean {
            val boxedBooleanType = context.utils.getDeclaredType(java.lang.Boolean::class)
            return context.utils.isAssignableType(type, boxedBooleanType)
        }
    }
}
