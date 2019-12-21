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

package io.t28.auto.truth.compiler

import com.squareup.javapoet.TypeName
import java.lang.reflect.Type
import javax.lang.model.type.TypeMirror

class TypeWrapper(private val name: TypeName) {
    val isVoid: Boolean
        get() = name == TypeName.VOID

    inline fun <reified T : Any> isClassOf(): Boolean {
        return isClassOf(TypeReference.getType<T>())
    }

    fun isClassOf(type: Type): Boolean {
        return name == TypeName.get(type)
    }

    fun asTypeName(): TypeName {
        return name
    }

    companion object {
        fun wrap(mirror: TypeMirror): TypeWrapper {
            return TypeWrapper(TypeName.get(mirror))
        }

        inline fun <reified T : Any> wrap(): TypeWrapper {
            val type = TypeReference.getType<T>()
            return TypeWrapper(TypeName.get(type))
        }
    }
}
