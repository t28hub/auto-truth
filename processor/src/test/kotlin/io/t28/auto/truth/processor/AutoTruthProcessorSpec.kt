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
import javax.tools.StandardLocation
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
            describe("prefix") {
                it("should append prefix when custom prefix is given") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "ValidPrefixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(StandardLocation.CLASS_OUTPUT, "io.t28.auto.truth.test", "Prefix\$ValidPrefixUserSubject.class")
                }

                it("should not append prefix when custom prefix is empty") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "EmptyPrefixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(StandardLocation.CLASS_OUTPUT, "io.t28.auto.truth.test", "EmptyPrefixUserSubject$.class")
                }

                it("should compile with error when given prefix is invalid") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "InvalidPrefixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .failsToCompile()
                        .withErrorContaining("Prefix given within @AutoTruth is invalid: 1Prefix")
                }
            }

            describe("suffix") {
                it("should append suffix when custom suffix is given") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "ValidSuffixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(StandardLocation.CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoValidSuffixUserSubject\$Suffix.class")
                }

                it("should not append suffix when custom suffix is empty") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "EmptySuffixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(StandardLocation.CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoEmptySuffixUserSubject.class")
                }

                it("should compile with error when given suffix is invalid") {
                    // Arrange
                    val valueJavaFile = "User.java".loadResource()
                    val subjectJavaFile = "InvalidSuffixUserSubject.java".loadResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile, subjectJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .failsToCompile()
                        .withErrorContaining("Suffix given within @AutoTruth is invalid: Suffix%")
                }
            }

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
