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

package io.t28.auto.truth.processor.generator.method

import com.google.common.truth.Truth.assertThat
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.extensions.findFields
import io.t28.auto.truth.processor.testing.MethodSpecSubject.Companion.assertThat
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import javax.lang.model.element.Modifier.PUBLIC
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BooleanAssertionGeneratorTest {
    @Nested
    @DisplayName("matches")
    inner class MatchesTest {
        @Test
        fun `should return true when type is boolean`() {
            process(Resource.PrimitiveTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.PrimitiveTypes.qualifiedName)
                val booleanProperty = element.findFields { field ->
                    field.simpleName.contentEquals("booleanValue")
                }.map { method -> Property.get(method) }.first()
                val generator = BooleanAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.isSupported(booleanProperty)

                // Assert
                assertThat(actual).isTrue()
            }.compilesWithoutError()
        }

        @Test
        fun `should return true when type is boxed boolean`() {
            process(Resource.BoxedPrimitiveTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.BoxedPrimitiveTypes.qualifiedName)
                val boxedBooleanProperty = element.findFields { fields ->
                    fields.simpleName.contentEquals("booleanValue")
                }.map { method -> Property.get(method) }.first()
                val generator = BooleanAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.isSupported(boxedBooleanProperty)

                // Assert
                assertThat(actual).isTrue()
            }.compilesWithoutError()
        }

        @Test
        fun `should return false when type is non-boolean`() {
            process(Resource.PrimitiveTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.PrimitiveTypes.qualifiedName)
                val nonBooleanProperty = element.findFields { field ->
                    field.simpleName.contentEquals("byteValue")
                }.map { method -> Property.get(method) }.first()
                val generator = BooleanAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.isSupported(nonBooleanProperty)

                // Assert
                assertThat(actual).isFalse()
            }.compilesWithoutError()
        }
    }

    @Nested
    @DisplayName("generate")
    inner class GenerateTest {
        @Test
        fun `should generate positive assertion method`() {
            process(Resource.PrimitiveTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.PrimitiveTypes.qualifiedName)
                val booleanProperty = element.findFields { field ->
                    field.simpleName.contentEquals("booleanValue")
                }.map { method -> Property.get(method) }.first()
                val generator = BooleanAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.generate(booleanProperty)

                // Assert
                assertThat(actual).apply {
                    hasName("isBooleanValue")
                    modifiers().contains(PUBLIC)
                    hasReturnType(TypeName.VOID)
                    parameters().isEmpty()
                }
            }.compilesWithoutError()
        }

        @Test
        fun `should generate negative assertion method`() {
            process(Resource.PrimitiveTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.PrimitiveTypes.qualifiedName)
                val booleanProperty = element.findFields { field ->
                    field.simpleName.contentEquals("booleanValue")
                }.map { method -> Property.get(method) }.first()
                val generator = BooleanAssertionGenerator.NegativeAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.generate(booleanProperty)

                // Assert
                assertThat(actual).apply {
                    hasName("isNotBooleanValue")
                    modifiers().contains(PUBLIC)
                    hasReturnType(TypeName.VOID)
                    parameters().isEmpty()
                }
            }.compilesWithoutError()
        }
    }
}
