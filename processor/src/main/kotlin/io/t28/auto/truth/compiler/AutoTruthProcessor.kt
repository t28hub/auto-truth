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

import io.t28.auto.truth.AutoSubject
import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import io.t28.auto.truth.compiler.extensions.asTypeElement
import io.t28.auto.truth.compiler.extensions.getAnnotatedElements
import io.t28.auto.truth.compiler.extensions.getPackage
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AutoTruthProcessor : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AutoSubject::class.java.canonicalName)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            return true
        }

        roundEnv.getAnnotatedElements<AutoSubject>()
                .filterIsInstance<TypeElement>()
                .forEach { element ->
                    val qualifiedName = "${element.qualifiedName}"
                    val packageName = "${element.getPackage().qualifiedName}"
                    val className = qualifiedName.removePrefix("${packageName}.")
                    val normalizedName = className.replace('$', '_')
                    val typeSpec = TypeSpec.classBuilder("Auto_${normalizedName}")
                            .addModifiers(Modifier.PUBLIC)
                            .build()

                    val javaFile = JavaFile.builder(packageName, typeSpec)
                            .indent("    ")
                            .skipJavaLangImports(true)
                            .build()
                    javaFile.writeTo(processingEnv.filer)
                }
        return true
    }
}
