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

package io.t28.auto.truth.processor

import io.t28.auto.truth.processor.log.Logger
import io.t28.auto.truth.processor.log.ProcessingEnvLogger
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

interface Context {
    companion object {
        private const val DEBUG_OPTION = "debug"

        fun get(processingEnv: ProcessingEnvironment): Context {
            val options = processingEnv.options
            return object : Context {
                override val logger: Logger
                    get() = ProcessingEnvLogger(processingEnv.messager, options.containsKey(DEBUG_OPTION))

                override fun getTypeElement(type: KClass<*>): TypeElement {
                    return processingEnv.elementUtils.getTypeElement(type.java.canonicalName)
                }

                override fun getArrayType(componentType: TypeMirror): ArrayType {
                    return processingEnv.typeUtils.getArrayType(componentType)
                }

                override fun erasureType(type: TypeMirror): TypeMirror {
                    return processingEnv.typeUtils.erasure(type)
                }

                override fun isInherited(first: TypeMirror, second: TypeMirror): Boolean {
                    return processingEnv.typeUtils.isAssignable(first, second)
                }
            }
        }
    }

    val logger: Logger

    fun getTypeElement(type: KClass<*>): TypeElement

    fun getArrayType(componentType: TypeMirror): ArrayType

    fun erasureType(type: TypeMirror): TypeMirror

    fun isInherited(first: TypeMirror, second: TypeMirror): Boolean
}
