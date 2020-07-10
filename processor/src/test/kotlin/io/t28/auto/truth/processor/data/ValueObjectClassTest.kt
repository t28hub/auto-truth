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

import com.google.auto.common.MoreTypes
import com.google.common.truth.Truth.assertThat
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import javax.lang.model.type.DeclaredType
import org.junit.jupiter.api.Test

internal class ValueObjectClassTest {
    @Test
    fun `should instantiate from TypeElement`() {
        process(Resource.User, Resource.UserSubject) {
            // Arrange
            val userElement = it.getTypeElement(Resource.User.qualifiedName)

            // Act
            val actual = ValueObjectClass(userElement)

            // Assert
            assertThat(actual.type).isInstanceOf(DeclaredType::class.java)
            assertThat(MoreTypes.asElement(actual.type)).isEqualTo(userElement)
            assertThat(actual.simpleName).isEqualTo("User")
            assertThat(actual.findProperties()).hasSize(5)
            assertThat(actual.findEnumConstants()).isEmpty()
        }.compilesWithoutError()
    }
}
