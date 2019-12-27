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

import com.squareup.javapoet.CodeBlock

class CodeBlockDsl internal constructor(private val builder: CodeBlock.Builder = CodeBlock.builder()) {
    fun statement(format: String, vararg args: Any?) {
        builder.addStatement(format, *args)
    }

    fun begin(statement: String, vararg args: Any?, init: CodeBlockDsl.() -> Unit): CodeBlockDsl {
        builder.beginControlFlow(statement, *args)
        CodeBlockDsl(builder).apply(init)
        return this
    }

    fun next(statement: String, vararg args: Any?, init: CodeBlockDsl.() -> Unit): CodeBlockDsl {
        builder.nextControlFlow(statement, *args)
        CodeBlockDsl(builder).apply(init)
        return this
    }

    fun end() {
        builder.endControlFlow()
    }

    fun `if`(statement: String, vararg args: Any?, init: CodeBlockDsl.() -> Unit): CodeBlockDsl {
        return begin("if ($statement)", args = *args, init = init)
    }

    internal fun build(): CodeBlock {
        return builder.build()
    }
}
