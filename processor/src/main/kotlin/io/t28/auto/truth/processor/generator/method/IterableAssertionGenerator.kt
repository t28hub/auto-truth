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

import io.t28.auto.truth.processor.Context
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleTypeVisitor8

abstract class IterableAssertionGenerator(protected val context: Context) : MethodGenerator {
    override fun matches(type: TypeMirror): Boolean {
        return type.accept(object : SimpleTypeVisitor8<Boolean, Unit>() {
            override fun visitDeclared(type: DeclaredType, parameter: Unit): Boolean {
                if (!context.utils.isIterable(type)) {
                    return false
                }

                val typeArguments = type.typeArguments
                return typeArguments.size == 1
            }

            override fun defaultAction(type: TypeMirror, parameter: Unit) = false
        }, Unit)
    }
}
