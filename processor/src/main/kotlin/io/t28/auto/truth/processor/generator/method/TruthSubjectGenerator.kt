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

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeMirror

abstract class TruthSubjectGenerator(protected val context: Context) : MethodGenerator {
    protected abstract fun subjectClass(type: TypeMirror): TypeName

    override fun generate(input: Property): MethodSpec {
        require(matches(input.type))
        context.logger.debug(input.element, "Generating a method returns Subject for ${input.type}")

        val subjectClass = subjectClass(input.type)
        val symbol = input.symbol
        return MethodSpec.methodBuilder(input.name.decapitalize()).apply {
            returns(subjectClass)
            addModifiers(Modifier.PUBLIC)
            addStatement("return check(\$S).that(\$L.\$L)", symbol, "actual", symbol)
        }.build()
    }
}
