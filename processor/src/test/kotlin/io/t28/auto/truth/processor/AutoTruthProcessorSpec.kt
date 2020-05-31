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
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects.forResource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import io.t28.auto.truth.processor.util.fixture
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import javax.tools.StandardLocation.CLASS_OUTPUT
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun String.loadResource(path: String = "io/t28/auto/truth/test"): JavaFileObject {
    return forResource("$path/$this")
}

private fun process(vararg fileNames: String): CompileTester {
    return process(*fileNames.map { name ->
        name.loadResource()
    }.toTypedArray())
}

private fun process(vararg fileObjects: JavaFileObject): CompileTester {
    return assertAbout(javaSources())
        .that(setOf(*fileObjects))
        .withCompilerOptions("-Adebug")
        .processedWith(AutoTruthProcessor())
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
                    // Act & Assert
                    process("User.java", "ValidPrefixUserSubject.java")
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "Prefix\$ValidPrefixUserSubject.class")
                }

                it("should not append prefix when custom prefix is empty") {
                    // Act & Assert
                    process("User.java", "EmptyPrefixUserSubject.java")
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "EmptyPrefixUserSubject$.class")
                }

                it("should compile with error when given prefix is invalid") {
                    // Act & Assert
                    process("User.java", "InvalidPrefixUserSubject.java")
                        .failsToCompile()
                        .withErrorContaining("Prefix given within @AutoTruth is invalid: 1Prefix")
                }
            }

            describe("suffix") {
                it("should append suffix when custom suffix is given") {
                    // Act & Assert
                    process("User.java", "ValidSuffixUserSubject.java")
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoValidSuffixUserSubject\$Suffix.class")
                }

                it("should not append suffix when custom suffix is empty") {
                    // Act & Assert
                    process("User.java", "EmptySuffixUserSubject.java")
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoEmptySuffixUserSubject.class")
                }

                it("should compile with error when given suffix is invalid") {
                    // Act & Assert
                    process("User.java", "InvalidSuffixUserSubject.java")
                        .failsToCompile()
                        .withErrorContaining("Suffix given within @AutoTruth is invalid: Suffix%")
                }
            }

            it("should generate Subject class") {
                // Arrange
                val userSubjectFile = "AutoUserSubject.java".loadResource()
                val userTypeSubjectFile = "AutoUserTypeSubject.java".loadResource()

                // Act & Assert
                process("User.java", "UserSubject.java", "UserTypeSubject.java")
                    .compilesWithoutError()
                    .and()
                    .generatesSources(userSubjectFile, userTypeSubjectFile)
            }

            arrayOf(
                fixture("array", "ArrayProperties.java", "ArrayPropertiesSubject.java", "AutoArrayPropertiesSubject.class"),
                fixture("iterable", "IterableProperties.java", "IterablePropertiesSubject.java", "AutoIterablePropertiesSubject.class"),
                fixture("map", "MapProperties.java", "MapPropertiesSubject.java", "AutoMapPropertiesSubject.class"),
                fixture("optional", "OptionalProperties.java", "OptionalPropertiesSubject.java", "AutoOptionalPropertiesSubject.class"),
                fixture("stream", "StreamProperties.java", "StreamPropertiesSubject.java", "AutoStreamPropertiesSubject.class"),
                fixture("guava", "GuavaProperties.java", "GuavaPropertiesSubject.java", "AutoGuavaPropertiesSubject.class"),
                fixture("enum", "EnumTypes.java", "EnumTypesSubject.java", "AutoEnumTypesSubject.class")
            ).forEach { (simpleName, valueObject, subject, expected) ->
                it("should compile $simpleName properties") {
                    // Act & Assert
                    process(valueObject, subject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", expected)
                }
            }
        }
    }
})
