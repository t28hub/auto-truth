/*
 * Copyright 2019 Tatsuya Maki
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

package io.t28.auto.truth.processor

import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.JavaFileObjects.forResource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun String.loadResource(path: String = "io/t28/auto/truth/test"): JavaFileObject {
    return forResource("$path/$this")
}

object AutoTruthProcessorSpec : Spek({
    describe("Processor") {
        describe("getSupportedOptions") {
            it("should return supported options") {
                // Act
                val processor = AutoTruthProcessor()
                val actual = processor.supportedOptions

                // Assert
                assertThat(actual).apply {
                    hasSize(1)
                    contains("debug")
                }
            }
        }

        describe("getSupportedSourceVersion") {
            it("should return latest source version") {
                // Act
                val processor = AutoTruthProcessor()
                val actual = processor.supportedSourceVersion

                // Assert
                assertThat(actual)
                    .isEqualTo(SourceVersion.latestSupported())
            }
        }

        describe("getSupportedAnnotationTypes") {
            it("should return a set that contains @AutoSubject only") {
                // Act
                val processor = AutoTruthProcessor()
                val actual = processor.supportedAnnotationTypes

                // Assert
                assertThat(actual)
                    .containsExactly("io.t28.auto.truth.AutoSubject")
            }
        }

        describe("process") {
            it("should generate Subject class") {
                // Arrange
                val valueJavaFile = "User.java".loadResource()
                val subjectJavaFile = "UserSubject.java".loadResource()
                val expectedJavaFile = "AutoUserSubject.java".loadResource()

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(valueJavaFile, subjectJavaFile))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesSources(expectedJavaFile)
            }
        }
    }
})
