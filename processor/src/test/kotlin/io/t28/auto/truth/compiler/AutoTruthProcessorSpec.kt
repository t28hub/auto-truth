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

package io.t28.auto.truth.compiler

import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.JavaFileObjects.forSourceString
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import javax.lang.model.SourceVersion

object AutoTruthProcessorSpec : Spek({
    describe("Processor") {
        val processor = AutoTruthProcessor()

        describe("getSupportedSourceVersion") {
            it("should return latest source version") {
                // Act
                val actual = processor.supportedSourceVersion

                // Assert
                assertThat(actual)
                        .isEqualTo(SourceVersion.latestSupported())
            }
        }

        describe("getSupportedAnnotationTypes") {
            it("should return a set that contains @AutoSubject only") {
                // Act
                val actual = processor.supportedAnnotationTypes

                // Assert
                assertThat(actual)
                        .containsExactly("io.t28.auto.truth.AutoSubject")
            }
        }

        describe("process") {
            it("should generate a class with 'Auto_' prefix and 'Subject' suffix") {
                // Arrange
                val subject = forSourceString(
                        "test.TestValue",
                        """
                            package test;
                            
                            import io.t28.auto.truth.AutoSubject;
                            
                            @AutoSubject
                            public class TestValue {
                                public boolean isOk;
                            }
                        """.trimIndent())

                val generated = forSourceString(
                        "test.Auto_TestValueSubject",
                        """
                            package test;
                            
                            import com.google.common.truth.FailureMetadata;
                            import com.google.common.truth.Subject;
                            
                            public class Auto_TestValueSubject <T extends TestValue> extends Subject {
                                private final TestValue actual;

                                private Auto_TestValueSubject(FailureMetadata failureMetadata, TestValue actual) {
                                    super(failureMetadata, actual);
                                    this.actual = actual;
                                }
                                
                                public static Subject.Factory<Auto_TestValueSubject, TestValue> testValue() {
                                    return Auto_TestValueSubject::new;
                                }
                            }
                        """.trimIndent()

                )

                // Act & Assert
                assertAbout(javaSources())
                        .that(setOf(subject))
                        .processedWith(processor)
                        .compilesWithoutError()
                        .and()
                        .generatesSources(generated)
            }
        }
    }
})
