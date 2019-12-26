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

package io.t28.auto.truth.compiler.writer

import com.squareup.javapoet.JavaFile
import io.t28.auto.truth.compiler.ClassDeclaration
import javax.annotation.processing.ProcessingEnvironment

class ProcessingEnvWriter(private val processingEnv: ProcessingEnvironment) : Writer {
    companion object {
        private const val INDENT = "    "
    }

    override fun write(declaration: ClassDeclaration) {
        val javaFile = JavaFile.builder(declaration.packageName, declaration.toSpec())
                .indent(INDENT)
                .skipJavaLangImports(true)
                .build()
        javaFile.writeTo(processingEnv.filer)
    }
}