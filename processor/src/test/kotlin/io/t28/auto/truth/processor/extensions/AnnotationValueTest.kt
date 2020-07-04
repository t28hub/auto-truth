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
import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import io.t28.auto.truth.processor.testing.Resource.AnnotatedClass
import io.t28.auto.truth.processor.testing.Resource.CustomAnnotation
import io.t28.auto.truth.processor.testing.TestProcessor
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.type.TypeMirror
import org.junit.jupiter.api.Test

internal class AnnotationValueTest {
    @Test
    fun `asString should return value as String`() {
        process { annotationMirror ->
            // Act
            val annotationValue = AnnotationMirrors.getAnnotationValue(annotationMirror, "stringValue")

            // Assert
            assertThat(annotationValue.asString())
                .isEqualTo("foobarbaz")
        }.compilesWithoutError()
    }

    @Test
    fun `asType should return value as Type`() {
        process { annotationMirror ->
            // Act
            val annotationValue = AnnotationMirrors.getAnnotationValue(annotationMirror, "classValue")

            // Assert
            assertThat(annotationValue.asType())
                .isInstanceOf(TypeMirror::class.java)
        }.compilesWithoutError()
    }

    private fun process(handler: (annotationMirror: AnnotationMirror) -> Unit): CompileTester {
        return assertAbout(javaSources())
            .that(listOf(CustomAnnotation.toJavaFileObject(), AnnotatedClass.toJavaFileObject()))
            .processedWith(TestProcessor.builder()
                .annotations(CustomAnnotation.qualifiedName)
                .nextHandler { context ->
                    val annotatedElement = context.getTypeElement(AnnotatedClass.qualifiedName)
                    val annotationMirror = annotatedElement.annotationMirrors.first { mirror ->
                        val annotationType = mirror.annotationType.asTypeElement()
                        annotationType.qualifiedName.contentEquals(CustomAnnotation.qualifiedName)
                    }
                    handler(annotationMirror)
                    false
                }
                .build())
    }
}
