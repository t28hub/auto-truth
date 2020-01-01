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
import io.t28.auto.truth.processor.utils.ProcessingEnvTypeUtils
import io.t28.auto.truth.processor.utils.TypeUtils
import javax.annotation.processing.ProcessingEnvironment

interface Context {
    companion object {
        private const val DEBUG_OPTION = "debug"

        fun get(processingEnv: ProcessingEnvironment): Context {
            return ProcessingContext(processingEnv)
        }
    }

    val logger: Logger

    val utils: TypeUtils

    class ProcessingContext(private val processingEnv: ProcessingEnvironment) : Context {
        override val logger: Logger by lazy {
            ProcessingEnvLogger(processingEnv.messager, processingEnv.options.containsKey(DEBUG_OPTION))
        }

        override val utils: TypeUtils by lazy {
            ProcessingEnvTypeUtils(processingEnv.typeUtils, processingEnv.elementUtils)
        }
    }
}
