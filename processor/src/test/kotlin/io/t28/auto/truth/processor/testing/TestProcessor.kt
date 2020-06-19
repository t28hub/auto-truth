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

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import io.t28.auto.truth.AutoSubject
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.JavaFileObject
import kotlin.reflect.KClass

fun process(vararg resources: Resource, handler: (TestContext) -> Unit): CompileTester {
    return process(files = *resources.map { it.toJavaFileObject() }.toTypedArray(), handler = handler)
}

fun process(vararg files: JavaFileObject, handler: (TestContext) -> Unit): CompileTester {
    return assertAbout(javaSources())
        .that(files.toList())
        .processedWith(TestProcessor.builder()
            .annotations(AutoSubject::class)
            .nextHandler { context ->
                handler(context)
                true
            }
            .build())
}

class TestProcessor private constructor(
    private val handlers: List<(TestContext) -> Boolean>,
    private val annotations: Set<String>
) : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return annotations
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            return false
        }
        val context = TestContext(processingEnv, roundEnv, annotations)
        return handlers.map { it(context) }.any()
    }

    class Builder(
        private val handlers: MutableList<(TestContext) -> Boolean> = mutableListOf(),
        private val annotations: MutableSet<String> = mutableSetOf()
    ) {
        fun nextHandler(handler: (TestContext) -> Boolean): Builder {
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
