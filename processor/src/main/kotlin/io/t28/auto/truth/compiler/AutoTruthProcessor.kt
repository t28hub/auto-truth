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

import com.google.auto.service.AutoService
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.compiler.element.AnnotatedTypeElement
import io.t28.auto.truth.compiler.element.AutoSubjectAnnotation
import io.t28.auto.truth.compiler.extensions.getAnnotatedElements
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedOptions("debug")
class AutoTruthProcessor : AbstractProcessor() {
    private lateinit var context: Context

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AutoSubject::class.java.canonicalName)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        context = Context.get(processingEnv)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            return true
        }

        val logger = context.logger
        val writer = context.writer
        val typeUtils = processingEnv.typeUtils
        val elementUtils = processingEnv.elementUtils
        roundEnv.getAnnotatedElements<AutoSubject>()
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                logger.debug(element, "Found annotated class: %s", element.simpleName)
                val annotated = AnnotatedTypeElement(element)
                val annotation = elementUtils.getAllAnnotationMirrors(element)
                    .first { annotationMirror ->
                        val annotationElement = typeUtils.asElement(annotationMirror.annotationType)
                        annotationElement.simpleName.contentEquals(AutoSubject::class.java.simpleName)
                    }
                    .let { annotationMirror -> AutoSubjectAnnotation(annotationMirror, elementUtils) }

                val declaration = SubjectClass(annotated, annotation)
                @Suppress("TooGenericExceptionCaught")
                try {
                    writer.write(declaration)
                } catch (e: Exception) {
                    logger.error(element, "Failed to compile: %s", "${e.message}")
                }
            }
        return true
    }
}
