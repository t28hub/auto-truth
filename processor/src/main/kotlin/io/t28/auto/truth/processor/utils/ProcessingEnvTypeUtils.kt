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

package io.t28.auto.truth.processor.utils

import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

class ProcessingEnvTypeUtils(
    private val types: Types,
    private val elements: Elements
) : TypeUtils {
    private val boxedVoidType: TypeMirror by lazy {
        getDeclaredType(java.lang.Void::class)
    }

    private val boxedBooleanType: TypeMirror by lazy {
        getDeclaredType(java.lang.Boolean::class)
    }

    private val iterableType: TypeMirror by lazy {
        getDeclaredType(Iterable::class)
    }

    private val mapType: TypeMirror by lazy {
        getDeclaredType(Map::class)
    }

    override fun isBoxedVoid(type: TypeMirror): Boolean {
        return types.isSameType(type, boxedVoidType)
    }

    override fun isBoxedBoolean(type: TypeMirror): Boolean {
        return types.isSameType(type, boxedBooleanType)
    }

    override fun isIterable(type: TypeMirror): Boolean {
        return types.isAssignable(type, iterableType)
    }

    override fun isMap(type: TypeMirror): Boolean {
        return types.isAssignable(type, mapType)
    }

    override fun getArrayType(componentType: TypeMirror): ArrayType {
        return types.getArrayType(componentType)
    }

    private fun getDeclaredType(type: KClass<*>): DeclaredType {
        val typeArgs = type.typeParameters.map {
            types.getWildcardType(null, null)
        }.toTypedArray()
        val element = elements.getTypeElement(type.java.canonicalName)
        return try {
            types.getDeclaredType(element, *typeArgs)
        } catch (e: IllegalArgumentException) {
            types.getDeclaredType(element)
        }
    }
}
