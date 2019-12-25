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

import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("UnnecessaryAbstractClass")
abstract class TypeReference<T> {
    internal val type by lazy {
        val superclass = this::class.java.genericSuperclass
        require(superclass !is Class<*>) {
            "TypeReference must be instantiate with actual type: $this"
        }

        (superclass as? ParameterizedType)
                ?.actualTypeArguments
                ?.firstOrNull() ?: throw IllegalStateException("Cannot get type argument from $this")
    }

    companion object {
        inline fun <reified T : Any> getType(): Type {
            val reference = object : TypeReference<T>() {}
            return reference.type
        }
    }
}
