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
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import javax.lang.model.type.TypeKind.BOOLEAN
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class TypeMirrorTest {
    @Test
    fun `should return true when type is NULL`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val typeMirror = it.processingEnv.typeUtils.nullType

            // Act
            val actual = typeMirror.isNull

            // Assert
            assertThat(actual).isTrue()
        }.compilesWithoutError()
    }
    @Test
    fun `should return true when type is not NULL`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val typeMirror = it.processingEnv.typeUtils.getPrimitiveType(BOOLEAN)

            // Act
            val actual = typeMirror.isNull

            // Assert
            assertThat(actual).isFalse()
        }.compilesWithoutError()
    }

    @ParameterizedTest
    @MethodSource("provideBoxedPrimitiveTypes")
    fun `should return true when type is BoxedPrimitive`(canonicalName: String) {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val element = it.getTypeElement(canonicalName)
            val typeMirror = element.asType()

            // Act
            val actual = typeMirror.isBoxedPrimitive()

            // Assert
            assertThat(actual).isTrue()
        }.compilesWithoutError()
    }

    @Test
    fun `should return false when type is PrimitiveType`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val typeMirror = it.processingEnv.typeUtils.getPrimitiveType(BOOLEAN)

            // Act
            val actual = typeMirror.isBoxedPrimitive()

            // Assert
            assertThat(actual).isFalse()
        }.compilesWithoutError()
    }

    @Test
    fun `should return false when type is NoType`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val typeElement = it.getTypeElement("java.lang.Void")
            val typeMirror = typeElement.asType()

            // Act
            val actual = typeMirror.isBoxedPrimitive()

            // Assert
            assertThat(actual).isFalse()
        }.compilesWithoutError()
    }

    @Test
    fun `should return false when type is DeclaredType`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val typeElement = it.getTypeElement("java.lang.String")
            val typeMirror = typeElement.asType()

            // Act
            val actual = typeMirror.isBoxedPrimitive()

            // Assert
            assertThat(actual).isFalse()
        }.compilesWithoutError()
    }

    @Test
    fun `asTypeElement should return TypeElement`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val userType = it.processingEnv.typeUtils.getDeclaredType(userElement)

            // Act
            val actual = userType.asTypeElement()

            // Assert
            assertThat(actual).isEqualTo(userElement)
        }.compilesWithoutError()
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        fun provideBoxedPrimitiveTypes(): Array<String> {
            return arrayOf(
                "java.lang.Boolean",
                "java.lang.Byte",
                "java.lang.Character",
                "java.lang.Short",
                "java.lang.Integer",
                "java.lang.Long",
                "java.lang.Float",
                "java.lang.Double"
            )
        }
    }
}
