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

import com.google.common.base.Preconditions
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.Modifier
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

abstract class Truth8SubjectGenerator(protected val context: Context) : MethodGenerator {
    final override fun isSupported(property: Property): Boolean {
        return object : SupportedTypeMatcher<Void?>() {
            override fun visitDeclared(type: DeclaredType, p: Void?): Boolean = matches(type)
        }.visit(property.type)
    }

    final override fun generate(input: Property): MethodSpec {
        require(isSupported(input))
        context.logger.debug(input.element, "Generating a method returns Truth8's Subject for ${input.type}")

        val subjectClass = subjectClass(input.type)
        val factoryMethod = "${factoryMethodName(input.type)}()"
        val symbol = input.symbol
        return MethodSpec.methodBuilder(input.name.decapitalize()).apply {
            returns(subjectClass)
            addModifiers(Modifier.PUBLIC)
            addStatement("final \$T actual = \$T.checkNotNull(this.actual).\$L", input.type, Preconditions::class.java, symbol)
            addStatement("return check(\$S).about(\$T.\$L).that(actual)", symbol, subjectClass, factoryMethod)
        }.build()
    }

    protected abstract fun matches(type: DeclaredType): Boolean

    protected abstract fun subjectClass(type: TypeMirror): TypeName

    protected abstract fun factoryMethodName(type: TypeMirror): String
}
