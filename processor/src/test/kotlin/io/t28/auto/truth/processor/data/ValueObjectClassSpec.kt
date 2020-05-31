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
import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects.forResource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.testing.TestContext
import io.t28.auto.truth.processor.testing.TestProcessor
import javax.lang.model.type.DeclaredType
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun compile(handler: (TestContext) -> Unit): CompileTester {
    return assertAbout(javaSources())
        .that(listOf(
            forResource("io/t28/auto/truth/test/User.java"),
            forResource("io/t28/auto/truth/test/UserSubject.java")
        ))
        .processedWith(TestProcessor.builder()
            .annotations(AutoSubject::class)
            .nextHandler { context ->
                handler(context)
                true
            }
            .build())
}

object ValueObjectClassSpec : Spek({
    describe("ValueObjectClass") {
        it("should return TypeMirror corresponding to TypeElement") {
            compile {
                // Arrange
                val userElement = it.getTypeElement("io.t28.auto.truth.test.User")
                val userValueObject = ValueObjectClass(userElement)

                // Act
                val actual = userValueObject.type

                // Assert
                assertThat(actual).isInstanceOf(DeclaredType::class.java)
                assertThat(MoreTypes.asElement(actual)).isEqualTo(userElement)
            }.compilesWithoutError()
        }

        it("should return simple name") {
            compile {
                // Arrange
                val typeElement = it.getTypeElement("io.t28.auto.truth.test.User.Type")
                val typeValueObject = ValueObjectClass(typeElement)

                // Act
                val actual = typeValueObject.simpleName

                // Assert
                assertThat(actual).isEqualTo("Type")
            }.compilesWithoutError()
        }

        it("should return accessible fields and getters") {
            compile {
                // Arrange
                val userElement = it.getTypeElement("io.t28.auto.truth.test.User")
                val userValueObject = ValueObjectClass(userElement)

                // Act
                val actual = userValueObject.properties

                // Assert
                assertThat(actual).hasSize(4)
            }
        }

        it("should return enum constants") {
            compile {
                // Arrange
                val typeElement = it.getTypeElement("io.t28.auto.truth.test.User.Type")
                val typeValueObject = ValueObjectClass(typeElement)

                // Act
                val actual = typeValueObject.enumConstants

                // Assert
                assertThat(actual).hasSize(2)
            }
        }
    }
})
