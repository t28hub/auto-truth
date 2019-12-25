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

package io.t28.auto.truth.compiler.dsl

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier

class MethodDsl private constructor(private val builder: MethodSpec.Builder) {
    companion object {
        fun constructor(): MethodDsl = MethodDsl(MethodSpec.constructorBuilder())

        fun method(name: String): MethodDsl = MethodDsl(MethodSpec.methodBuilder(name))
    }

    fun modifiers(vararg modifiers: Modifier) {
        builder.addModifiers(*modifiers)
    }

    fun params(vararg params: ParameterSpec) {
        builder.addParameters(params.asIterable())
    }

    fun statement(format: String, vararg args: Any) {
        builder.addStatement(format, *args)
    }

    infix fun returns(type: TypeName) {
        builder.returns(type)
    }

    fun build(): MethodSpec {
        return builder.build()
    }
}
