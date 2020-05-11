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

package io.t28.auto.truth.processor.testing

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

typealias Handler = (TestContext) -> Boolean

class TestProcessor private constructor(
    private val handlers: List<Handler>,
    private val annotations: Set<String>
) : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return annotations
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val context = TestContext(processingEnv, roundEnv, annotations)
        return handlers.map { it(context) }.any()
    }

    class Builder(
        private val handlers: MutableList<Handler> = mutableListOf(),
        private val annotations: MutableSet<String> = mutableSetOf()
    ) {
        fun nextHandler(handler: Handler): Builder {
            this.handlers.add(handler)
            return this
        }

        fun annotations(vararg annotations: KClass<*>): Builder {
            this.annotations.addAll(annotations.map { it.java.canonicalName })
            return this
        }

        fun annotations(vararg annotations: String): Builder {
            this.annotations.addAll(annotations)
            return this
        }

        fun build(): TestProcessor {
            return TestProcessor(handlers = handlers.toList(), annotations = annotations.toSet())
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
