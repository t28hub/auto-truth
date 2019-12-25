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

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import java.lang.reflect.Type
import javax.lang.model.element.Modifier

class FieldDsl private constructor(private val builder: FieldSpec.Builder) {
    internal constructor(type: Type, name: String) : this(FieldSpec.builder(type, name))

    internal constructor(type: TypeName, name: String) : this(FieldSpec.builder(type, name))

    fun modifiers(vararg modifiers: Modifier) {
        builder.addModifiers(*modifiers)
    }

    fun build(): FieldSpec {
        return builder.build()
    }
}
