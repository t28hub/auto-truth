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
import io.t28.auto.truth.processor.extensions.findEnumConstants
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.testing.MethodSpecSubject.Companion.assertThat
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import javax.lang.model.element.Modifier.PUBLIC
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EnumAssertionGeneratorTest {
    @Nested
    inner class MatchesTest {
        @Test
        fun `should return true when given type is EnumConstant`() {
            process(Resource.EnumTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.EnumTypes.qualifiedName)
                val property = element.findEnumConstants { constant ->
                    constant.simpleName.contentEquals("FOO_BAR")
                }.map { constant -> Property.get(constant) }.first()
                val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.matches(property)

                // Assert
                assertThat(actual).isTrue()
            }.compilesWithoutError()
        }

        @Test
        fun `should return false when given type is not EnumConstant`() {
            process(Resource.EnumTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.EnumTypes.qualifiedName)
                val property = element.findMethods { method ->
                    method.simpleName.contentEquals("values")
                }.map { method -> Property.get(method) }.first()
                val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.matches(property)

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
            process(Resource.EnumTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.EnumTypes.qualifiedName)
                val property = element.findEnumConstants { constant ->
                    constant.simpleName.contentEquals("FOO_BAR")
                }.map { constant -> Property.get(constant) }.first()
                val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.generate(property)

                // Assert
                assertThat(actual).apply {
                    hasName("isFooBar")
                    modifiers().contains(PUBLIC)
                    hasReturnType(TypeName.VOID)
                    parameters().isEmpty()
                }
            }.compilesWithoutError()
        }

        @Test
        fun `should generate negative assertion method`() {
            process(Resource.EnumTypes) {
                // Arrange
                val element = it.getTypeElement(Resource.EnumTypes.qualifiedName)
                val property = element.findEnumConstants { constant ->
                    constant.simpleName.contentEquals("FOO_BAR")
                }.map { constant -> Property.get(constant) }.first()
                val generator = EnumAssertionGenerator.NegativeAssertionGenerator(Context.get(it.processingEnv))

                // Act
                val actual = generator.generate(property)

                // Assert
                assertThat(actual).apply {
                    hasName("isNotFooBar")
                    modifiers().contains(PUBLIC)
                    hasReturnType(TypeName.VOID)
                    parameters().isEmpty()
                }
            }.compilesWithoutError()
        }
    }
}
