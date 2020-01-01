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

import com.google.common.truth.Fact
import com.squareup.javapoet.MethodSpec
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.Modifier

class BooleanNegativeAssertionGenerator(context: Context) : BooleanAssertionGenerator(context) {
    override fun generate(input: Property): MethodSpec {
        require(matches(input.type))
        context.logger.debug(input.element, "Generating an assertion method for boolean and Boolean")

        val name = input.name
        return MethodSpec.methodBuilder("isNot${name.capitalize()}").apply {
            addModifiers(Modifier.PUBLIC)

            beginControlFlow("if (this.\$L.\$L)", "actual", input.symbol).apply {
                addStatement("failWithActual(\$T.simpleFact(\$S))", Fact::class.java, "expected not to be $name")
            }
            endControlFlow()
        }.build()
    }
}
