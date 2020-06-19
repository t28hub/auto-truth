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
import io.t28.auto.truth.processor.testing.Resource.User
import io.t28.auto.truth.processor.testing.Resource.UserSubject
import io.t28.auto.truth.processor.testing.process
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SubjectClassSpec : Spek({
    describe("SubjectClass") {
        describe("simpleName") {
            it("should return name with prefix and suffix") {
                process(User, UserSubject) {
                    // Arrange
                    val userElement = it.getTypeElement(User.qualifiedName)
                    val userSubjectElement = it.getTypeElement(UserSubject.qualifiedName)

                    // Act
                    val subjectClass = SubjectClass(
                        packageName = UserSubject.packageName,
                        prefix = "Prefix",
                        suffix = "Suffix",
                        element = userSubjectElement,
                        valueObject = ValueObjectClass(userElement)
                    )
                    val simpleName = subjectClass.simpleName

                    // Assert
                    assertThat(simpleName).isEqualTo("PrefixUserSubjectSuffix")
                }.compilesWithoutError()
            }
        }
    }
})
