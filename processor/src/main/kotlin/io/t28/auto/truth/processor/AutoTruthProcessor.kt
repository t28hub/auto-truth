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
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.extensions.getAnnotatedElements
import io.t28.auto.truth.processor.generator.SubjectClassGenerator
import io.t28.auto.truth.processor.generator.method.AbstractArraySubjectGenerator
import io.t28.auto.truth.processor.generator.method.BooleanAssertionGenerator
import io.t28.auto.truth.processor.generator.method.ClassSubjectGenerator
import io.t28.auto.truth.processor.generator.method.EnumAssertionGenerator
import io.t28.auto.truth.processor.generator.method.GuavaOptionalSubjectGenerator
import io.t28.auto.truth.processor.generator.method.IterableAssertionGenerator
import io.t28.auto.truth.processor.generator.method.IterableSubjectGenerator
import io.t28.auto.truth.processor.generator.method.MapSubjectGenerator
import io.t28.auto.truth.processor.generator.method.MultimapSubjectGenerator
import io.t28.auto.truth.processor.generator.method.ObjectAssertionGenerator
import io.t28.auto.truth.processor.generator.method.OptionalSubjectGenerator
import io.t28.auto.truth.processor.generator.method.PathSubjectGenerator
import io.t28.auto.truth.processor.generator.method.StreamSubjectGenerator
import io.t28.auto.truth.processor.generator.method.TableSubjectGenerator
import io.t28.auto.truth.processor.processor.AutoSubjectProcessor
import io.t28.auto.truth.processor.processor.ProcessingException
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
        val processor = AutoSubjectProcessor(context)
        roundEnv.getAnnotatedElements<AutoSubject>()
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                logger.debug(element, "Found annotated class: %s", element.simpleName)
                try {
                    val subjectClass = processor.process(element)
                    generateJavaFile(subjectClass)
                } catch (e: ProcessingException) {
                    logger.error(e.element, e.message)
                }
            }
        return true
    }

    private fun generateJavaFile(subjectClass: SubjectClass) {
        val generator = SubjectClassGenerator(
            AbstractArraySubjectGenerator(context),
            BooleanAssertionGenerator.PositiveAssertionGenerator(context),
            BooleanAssertionGenerator.NegativeAssertionGenerator(context),
            ClassSubjectGenerator(context),
            GuavaOptionalSubjectGenerator(context),
            IterableSubjectGenerator(context),
            IterableAssertionGenerator.PositiveAssertionGenerator(context),
            IterableAssertionGenerator.NegativeAssertionGenerator(context),
            MapSubjectGenerator(context),
            MultimapSubjectGenerator(context),
            TableSubjectGenerator(context),
            EnumAssertionGenerator.PositiveAssertionGenerator(context),
            EnumAssertionGenerator.NegativeAssertionGenerator(context),
            ObjectAssertionGenerator(context),
            OptionalSubjectGenerator(context),
            StreamSubjectGenerator(context),
            PathSubjectGenerator(context)
        )
        val typeSpec = generator.generate(subjectClass)
        JavaFile.builder(subjectClass.packageName, typeSpec)
            .skipJavaLangImports(true)
            .indent("    ")
            .build().writeTo(processingEnv.filer)
    }
}
