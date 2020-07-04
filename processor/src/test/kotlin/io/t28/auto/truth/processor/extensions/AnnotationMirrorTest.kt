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

import com.google.common.truth.Truth.assertThat
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import org.junit.jupiter.api.Test

internal class AnnotationMirrorTest {
    @Test
    fun `getAnnotationValue should return specified value`() {
        process(Resource.User, Resource.ValidSuffixUserSubject) { context ->
            // Arrange
            val userSubject = context.getTypeElement(Resource.ValidSuffixUserSubject.qualifiedName)
            val autoSubject = userSubject.findAnnotationMirror<AutoSubject>()

            // Act
            val suffix = autoSubject?.getAnnotationValue("suffix")

            // Assert
            assertThat(suffix).apply {
                isNotNull()
                assertThat(suffix?.value).isEqualTo("\$Suffix")
            }
        }.compilesWithoutError()
    }

    @Test
    fun `should return default value`() {
        process(Resource.User, Resource.ValidSuffixUserSubject) { context ->
            // Arrange
            val userSubject = context.getTypeElement(Resource.ValidSuffixUserSubject.qualifiedName)
            val autoSubject = userSubject.findAnnotationMirror<AutoSubject>()

            // Act
            val prefix = autoSubject?.getAnnotationValue("prefix")

            // Assert
            assertThat(prefix).apply {
                isNotNull()
                assertThat(prefix?.value).isEqualTo("Auto")
            }
        }.compilesWithoutError()
    }

    @Test
    fun `should return null when name does not exist`() {
        process(Resource.User, Resource.ValidSuffixUserSubject) { context ->
            // Arrange
            val userSubject = context.getTypeElement(Resource.ValidSuffixUserSubject.qualifiedName)
            val autoSubject = userSubject.findAnnotationMirror<AutoSubject>()

            // Act
            val unknown = autoSubject?.getAnnotationValue("unknown")

            // Assert
            assertThat(unknown).isNull()
        }.compilesWithoutError()
    }
}
