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

package io.t28.auto.truth.processor.extensions

import com.google.auto.common.AnnotationMirrors
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.Compilation
import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private const val ANNOTATION_CLASS_NAME = "io.t28.auto.truth.test.CustomAnnotation"
private const val ANNOTATED_CLASS_NAME = "io.t28.auto.truth.test.AnnotatedClass"

private val ANNOTATION_CLASS = JavaFileObjects.forSourceString(ANNOTATION_CLASS_NAME, """
    package io.t28.auto.truth.test;
    
    @interface CustomAnnotation {
        Class<?> classValue();
        
        String stringValue();
    }
""".trimIndent())

private val ANNOTATED_CLASS = JavaFileObjects.forSourceString(ANNOTATED_CLASS_NAME, """
    package io.t28.auto.truth.test;
    
    @CustomAnnotation(
        classValue = String.class,
        stringValue = "foobarbaz"
    )
    class AnnotatedClass {
    }
""".trimIndent())

private class TestProcessor(private val processor: TestProcessor.() -> Boolean) : AbstractProcessor() {
    lateinit var elements: Elements
        private set

    lateinit var types: Types
        private set

    override fun init(processingEnv: ProcessingEnvironment) {
        elements = processingEnv.elementUtils
        types = processingEnv.typeUtils
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        return processor()
    }
}

private fun compile(invocation: (annotationMirror: AnnotationMirror) -> Unit) {
    val processor = TestProcessor {
        val annotatedElement = elements.getTypeElement(ANNOTATED_CLASS_NAME)
        val annotationMirror = annotatedElement.annotationMirrors.first { mirror ->
            val annotationType = mirror.annotationType.asTypeElement()
            annotationType.qualifiedName.contentEquals(ANNOTATION_CLASS_NAME)
        }
        invocation(annotationMirror)
        false
    }

    val compilation = javac()
        .withProcessors(processor)
        .compile(ANNOTATION_CLASS, ANNOTATED_CLASS)
    require(compilation.status() == Compilation.Status.SUCCESS)
}

object AnnotationValueSpec : Spek({
    describe("AnnotationValue") {
        describe("asString") {
            it("should return value as String") {
                compile { annotationMirror ->
                    // Act
                    val annotationValue = AnnotationMirrors.getAnnotationValue(annotationMirror, "stringValue")

                    // Assert
                    assertThat(annotationValue.asString())
                        .isEqualTo("foobarbaz")
                }
            }
        }

        describe("asType") {
            it("should return value as Type") {
                compile { annotationMirror ->
                    // Act
                    val annotationValue = AnnotationMirrors.getAnnotationValue(annotationMirror, "classValue")

                    // Assert
                    assertThat(annotationValue.asType())
                        .isInstanceOf(TypeMirror::class.java)
                }
            }
        }
    }
})
