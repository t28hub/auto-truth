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

package io.t28.auto.truth.processor.data

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assert_
import io.t28.auto.truth.processor.extensions.findEnumConstants
import io.t28.auto.truth.processor.extensions.findFields
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import java.lang.Exception
import javax.lang.model.type.PrimitiveType
import javax.lang.model.util.ElementFilter.constructorsIn
import javax.lang.model.util.ElementFilter.typesIn
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PropertyTest {
    @Test
    fun `should return Field when given element is field`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val fieldElement = userElement.findFields().first()

            // Act
            val actual = Property.get(fieldElement)

            // Assert
            assertThat(actual).isInstanceOf(Property.Field::class.java)
        }.compilesWithoutError()
    }

    @Test
    fun `should return Getter when given element is method`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val methodElement = userElement.findMethods().first()

            // Act
            val actual = Property.get(methodElement)

            // Assert
            assertThat(actual).isInstanceOf(Property.Getter::class.java)
        }.compilesWithoutError()
    }

    @Test
    fun `should return EnumConstant when given element is enum constant`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val userTypeElement = typesIn(userElement.enclosedElements).first()
            val enumConstantElement = userTypeElement.findEnumConstants().first()

            // Act
            val actual = Property.get(enumConstantElement)

            // Assert
            assertThat(actual).isInstanceOf(Property.EnumConstant::class.java)
        }.compilesWithoutError()
    }

    @Test
    fun `should throw exception when given element is parameter`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val constructorElement = constructorsIn(userElement.enclosedElements).first()
            val parameterElement = constructorElement.parameters.first()

            try {
                // Act
                Property.get(parameterElement)
                assert_().withMessage("Property.get should throw Exception").fail()
            } catch (e: Exception) {
                // Assert
                assertThat(e).hasMessageThat().contains("Unsupported element type: PARAMETER")
            }
        }.compilesWithoutError()
    }

    @Test
    fun `should throw exception when given element is constructor`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)
            val constructorElement = constructorsIn(userElement.enclosedElements).first()

            try {
                // Act
                Property.get(constructorElement)
                assert_().withMessage("Property.get should throw Exception").fail()
            } catch (e: Exception) {
                // Assert
                assertThat(e).hasMessageThat().contains("Unsupported element type: CONSTRUCTOR")
            }
        }.compilesWithoutError()
    }

    @Test
    fun `should throw exception when given element is not supported`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)

            try {
                // Act
                Property.get(userElement)
                assert_().withMessage("Property.get should throw Exception").fail()
            } catch (e: Exception) {
                // Assert
                assertThat(e).hasMessageThat().contains("Unsupported element type: CLASS")
            }
        }.compilesWithoutError()
    }

    @Nested
    @DisplayName("Field")
    inner class FieldTest {
        @Test
        fun `should instantiate from field`() {
            process(Resource.User, Resource.UserSubject) {
                // Arrange
                val userElement = it.getTypeElement(Resource.User.qualifiedName)
                val isAdminElement = userElement.findFields { field ->
                    field.simpleName.contentEquals("isAdmin")
                }.first()

                // Act
                val actual = Property.get(isAdminElement)

                // Assert
                assertThat(actual.type).isInstanceOf(PrimitiveType::class.java)
                assertThat(actual.name).isEqualTo("admin")
                assertThat(actual.symbol).isEqualTo("isAdmin")
            }.compilesWithoutError()
        }
    }

    @Nested
    @DisplayName("Getter")
    inner class GetterTest {
        @Test
        fun `should instantiate from method`() {
            process(Resource.User, Resource.UserSubject) {
                // Arrange
                val userElement = it.getTypeElement(Resource.User.qualifiedName)
                val getNameElement = userElement.findMethods { method ->
                    method.simpleName.contentEquals("getName")
                }.first()

                // Act
                val actual = Property.get(getNameElement)

                // Assert
                assertThat(actual.type).isEqualTo(getNameElement.returnType)
                assertThat(actual.name).isEqualTo("name")
                assertThat(actual.symbol).isEqualTo("getName()")
            }.compilesWithoutError()
        }
    }

    @Nested
    @DisplayName("EnumConstant")
    inner class EnumConstantTest {
        @Test
        fun `should instantiate from enum constant`() {
            process(Resource.User, Resource.UserSubject) {
                // Arrange
                val userElement = it.getTypeElement(Resource.User.qualifiedName)
                val userTypeElement = typesIn(userElement.enclosedElements).first()
                val enumConstantElement = userTypeElement.findEnumConstants().first { constant ->
                    constant.simpleName.contentEquals("GUEST")
                }

                // Act
                val actual = Property.get(enumConstantElement)

                // Assert
                assertThat(actual.type).isEqualTo(userTypeElement.asType())
                assertThat(actual.name).isEqualTo("guest")
                assertThat(actual.symbol).isEqualTo("GUEST")
            }.compilesWithoutError()
        }
    }
}
