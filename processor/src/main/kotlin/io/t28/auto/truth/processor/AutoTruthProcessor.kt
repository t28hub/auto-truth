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

import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.extensions.getAnnotatedElements
import io.t28.auto.truth.processor.processor.AutoSubjectProcessor
import io.t28.auto.truth.processor.processor.ExecutablePropertyProcessor
import io.t28.auto.truth.processor.processor.VariablePropertyProcessor
import io.t28.auto.truth.processor.translator.SubjectClassTranslator
import io.t28.auto.truth.processor.translator.property.ArrayPropertyTranslator
import io.t28.auto.truth.processor.translator.property.BooleanPropertyTranslator
import io.t28.auto.truth.processor.translator.property.DefaultPropertyTranslator
import io.t28.auto.truth.processor.translator.property.IterablePropertyTranslator
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
        val processor = AutoSubjectProcessor(context, VariablePropertyProcessor(context), ExecutablePropertyProcessor(context))
        val translator = SubjectClassTranslator(
            ArrayPropertyTranslator(),
            BooleanPropertyTranslator.PositiveTranslator(),
            BooleanPropertyTranslator.NegativeTranslator(),
            IterablePropertyTranslator(context),
            IterablePropertyTranslator.PositiveTranslator(context),
            IterablePropertyTranslator.NegativeTranslator(context),
            DefaultPropertyTranslator()
        )
        roundEnv.getAnnotatedElements<AutoSubject>()
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                logger.debug(element, "Found annotated class: %s", element.simpleName)
                @Suppress("TooGenericExceptionCaught")
                try {
                    val subjectClass = processor.process(element)
                    val typeSpec = translator.translate(subjectClass)
                    JavaFile.builder(subjectClass.packageName, typeSpec)
                        .skipJavaLangImports(true)
                        .indent("    ")
                        .build().writeTo(processingEnv.filer)
                } catch (e: Exception) {
                    logger.error(element, "Failed to compile: %s", "${e.message}")
                }
            }
        return true
    }
}
