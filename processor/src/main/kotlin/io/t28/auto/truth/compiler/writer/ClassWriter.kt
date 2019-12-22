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

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import io.t28.auto.truth.compiler.AutoTruthProcessor
import io.t28.auto.truth.compiler.ClassDeclaration
import javax.annotation.Generated
import javax.annotation.processing.ProcessingEnvironment

class ClassWriter(private val processingEnv: ProcessingEnvironment) {
    companion object {
        private const val INDENT = "    "
    }

    fun write(declaration: ClassDeclaration) {
        val builder = declaration.toSpec().toBuilder()
        processingEnv.elementUtils.getTypeElement(Generated::class.java.canonicalName)?.run {
            val annotationName = ClassName.get(this)
            builder.addAnnotation(AnnotationSpec.builder(annotationName)
                    .addMember("value", "\$S", AutoTruthProcessor::class.java.canonicalName)
                    .build())
        }

        builder.addAnnotation(AnnotationSpec.builder(SuppressWarnings::class.java)
                .addMember("value", "\$S", "unckecked")
                .build())

        val javaFile = JavaFile.builder(declaration.packageName, builder.build())
                .indent(INDENT)
                .skipJavaLangImports(true)
                .build()
        javaFile.writeTo(processingEnv.filer)
    }
}
