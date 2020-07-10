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

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.Modifier.PUBLIC

sealed class EnumAssertionGenerator(private val context: Context) : MethodGenerator {
    final override fun isSupported(property: Property): Boolean {
        return property is Property.EnumConstant
    }

    final override fun generate(input: Property): MethodSpec {
        require(isSupported(input))
        context.logger.debug(input.element, "Generating an assertion method for %s", input.type)
        return MethodSpec.methodBuilder(generateName(input)).apply {
            addModifiers(PUBLIC)
            addCode(generateCode(input))
        }.build()
    }

    abstract fun generateName(input: Property): String

    abstract fun generateCode(input: Property): CodeBlock

    class PositiveAssertionGenerator(context: Context) : EnumAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "is${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            return CodeBlock.builder().apply {
                beginControlFlow("if (actual != \$T.\$L)", input.type, input.symbol).apply {
                    addStatement("failWithActual(\$S, actual)", "expected to be ${input.symbol}")
                    endControlFlow()
                }
            }.build()
        }
    }

    class NegativeAssertionGenerator(context: Context) : EnumAssertionGenerator(context) {
        override fun generateName(input: Property): String {
            return "isNot${input.name.capitalize()}"
        }

        override fun generateCode(input: Property): CodeBlock {
            return CodeBlock.builder().apply {
                beginControlFlow("if (actual == \$T.\$L)", input.type, input.symbol).apply {
                    addStatement("failWithActual(\$S, actual)", "expected not to be ${input.symbol}")
                    endControlFlow()
                }
            }.build()
        }
    }
}
