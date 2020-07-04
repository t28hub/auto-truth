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
import com.google.common.truth.Truth.assert_
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.VariableElement
import org.junit.jupiter.api.Test

internal class ElementTest {
    @Test
    fun `isPublic should return true when modifiers contains PUBLIC`() {
        // Arrange
        val element = mock<Element> {
            on { modifiers } doReturn setOf(PUBLIC, STATIC, FINAL)
        }

        // Act
        val actual = element.isPublic

        // Assert
        assert_()
            .withMessage("Expected isPublic to be true, but was false")
            .that(actual)
            .isTrue()
    }

    @Test
    fun `isPublic should return false when modifiers does not contain PUBLIC`() {
        // Arrange
        val element = mock<Element> {
            on { modifiers } doReturn setOf(PRIVATE)
        }

        // Act
        val actual = element.isPublic

        // Assert
        assert_()
            .withMessage("Expected isPublic to be false, but was true")
            .that(actual)
            .isFalse()
    }

    @Test
    fun `isStatic should return true when modifiers contains STATIC`() {
        // Arrange
        val element = mock<Element> {
            on { modifiers } doReturn setOf(PUBLIC, STATIC)
        }

        // Act
        val actual = element.isStatic

        // Assert
        assert_()
            .withMessage("Expected isStatic to be true, but was false")
            .that(actual)
            .isTrue()
    }

    @Test
    fun `should return false when modifiers does not contain STATIC`() {
        // Arrange
        val element = mock<Element> {
            on { modifiers } doReturn setOf(PUBLIC)
        }

        // Act
        val actual = element.isStatic

        // Assert
        assert_()
            .withMessage("Expected isStatic to be false, but was true")
            .that(actual)
            .isFalse()
    }

    @Test
    fun `hasParameter should return true when parameters is not empty`() {
        // Arrange
        val element = mock<ExecutableElement> {
            on { parameters } doReturn mutableListOf(mock<VariableElement> {})
        }

        // Act
        val actual = element.hasParameter

        // Assert
        assert_()
            .withMessage("Expected hasParameter to be true, but was false")
            .that(actual)
            .isTrue()
    }

    @Test
    fun `hasParameter should return false when parameters is empty`() {
        // Arrange
        val element = mock<ExecutableElement> {
            on { parameters } doReturn mutableListOf()
        }

        // Act
        val actual = element.hasParameter

        // Assert
        assert_()
            .withMessage("Expected hasParameter to be false, but was true")
            .that(actual)
            .isFalse()
    }

    @Test
    fun `isEnum should return true when kind is ENUM`() {
        // Arrange
        val element = mock<ExecutableElement> {
            on { kind } doReturn ElementKind.ENUM
        }

        // Act
        val actual = element.isEnum

        // Assert
        assert_()
            .withMessage("Expected isEnum to be true, but was false")
            .that(actual)
            .isTrue()
    }

    @Test
    fun `isEnum should return false when kind is not ENUM`() {
        // Arrange
        val element = mock<ExecutableElement> {
            on { kind } doReturn ElementKind.CLASS
        }

        // Act
        val actual = element.isEnum

        // Assert
        assert_()
            .withMessage("Expected isEnum to be false, but was true")
            .that(actual)
            .isFalse()
    }

    @Test
    fun `getPackage should find PackageElement from TypeElement`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)

            // Act
            val actual = userElement.getPackage()

            // Assert
            assertThat("${actual.qualifiedName}").isEqualTo(Resource.User.packageName)
        }.compilesWithoutError()
    }
}
