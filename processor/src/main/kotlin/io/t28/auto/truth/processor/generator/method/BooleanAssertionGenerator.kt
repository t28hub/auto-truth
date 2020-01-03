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

import io.t28.auto.truth.processor.Context
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

abstract class BooleanAssertionGenerator(protected val context: Context) : MethodGenerator {
    override fun matches(type: TypeMirror): Boolean {
        return type.accept(BooleanTypeMatcher, context)
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
