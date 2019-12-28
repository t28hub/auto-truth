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
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import javax.tools.StandardLocation.CLASS_OUTPUT
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AutoTruthProcessorSpec : Spek({
    describe("Processor") {
        describe("getSupportedOptions") {
            it("should return") {
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

        describe("prefix") {
            fun createJavaFile(prefix: String? = null): JavaFileObject {
                // language=java
                val template = """
                    package test;
                            
                    import io.t28.auto.truth.AutoSubject;
                            
                    ${prefix?.let { """@AutoSubject(prefix = "$it")""" } ?: "@AutoSubject"}
                    public interface TestValue {
                        String name();
                    }
                """.trimIndent()
                return forSourceString("test.TestValue", template)
            }

            it("should generate class with default prefix") {
                // Arrange
                val subject = createJavaFile()

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "AutoTestValueSubject.class")
            }

            it("should generate class with specified prefix") {
                // Arrange
                val subject = createJavaFile("MyPrefix")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "MyPrefixTestValueSubject.class")
            }

            it("should generate class with no prefix") {
                // Arrange
                val subject = createJavaFile("")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "TestValueSubject.class")
            }

            it("should fail to compile when prefix contains invalid chars") {
                // Arrange
                val subject = createJavaFile("1nvalid")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .failsToCompile()
                    .withErrorContaining("1nvalidTestValueSubject")
            }
        }

        describe("suffix") {
            fun createJavaFile(suffix: String? = null): JavaFileObject {
                // language=java
                val template = """
                    package test;
                            
                    import io.t28.auto.truth.AutoSubject;
                            
                    ${suffix?.let { """@AutoSubject(suffix = "$it")""" } ?: "@AutoSubject"}
                    public interface TestValue {
                        String name();
                    }
                """.trimIndent()
                return forSourceString("test.TestValue", template)
            }

            it("should generate class with default suffix") {
                // Arrange
                val subject = createJavaFile()

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "AutoTestValueSubject.class")
            }

            it("should generate class with specified suffix") {
                // Arrange
                val subject = createJavaFile("MySubject")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "AutoTestValueMySubject.class")
            }

            it("should generate class with no suffix") {
                // Arrange
                val subject = createJavaFile("")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesFileNamed(CLASS_OUTPUT, "test", "AutoTestValue.class")
            }

            it("should fail to compile when suffix contains invalid chars") {
                // Arrange
                val subject = createJavaFile("@Subject")

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .failsToCompile()
                    .withErrorContaining("AutoTestValue@Subject")
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
                            public interface TestValue {
                                long id();
                                
                                String name();
                                
                                boolean isAdmin();
                                
                                Boolean isOwner();
                                
                                int[] values();
                                
                                String[] names();
                                
                                void accept();
                                
                                Void accept2();
                            }
                        """.trimIndent())

                val generated = forSourceString(
                    "test.AutoTestValueSubject",
                    // language=java
                    """
                            package test;
                            
                            import com.google.common.truth.Fact;
                            import com.google.common.truth.FailureMetadata;
                            import com.google.common.truth.Subject;
                            import javax.annotation.Generated;
                            
                            @Generated("io.t28.auto.truth.compiler.AutoTruthProcessor")
                            @SuppressWarnings("unchecked")
                            public class AutoTestValueSubject<T extends TestValue> extends Subject {
                                private final TestValue actual;

                                private AutoTestValueSubject(FailureMetadata failureMetadata, TestValue actual) {
                                    super(failureMetadata, actual);
                                    this.actual = actual;
                                }
                                
                                public static Subject.Factory<AutoTestValueSubject, TestValue> testValue() {
                                    return AutoTestValueSubject::new;
                                }
                                
                                public void hasId(long expected) {
                                    check("id()").that(this.actual.id()).isEqualTo(expected);
                                }

                                public void hasName(String expected) {
                                    check("name()").that(this.actual.name()).isEqualTo(expected);
                                }
                                
                                public void isAdmin() {
                                    if (!this.actual.isAdmin()) {
                                        failWithActual(Fact.simpleFact("expected to be admin"));
                                    }
                                }

                                public void isNotAdmin() {
                                    if (this.actual.isAdmin()) {
                                        failWithActual(Fact.simpleFact("expected not to be admin"));
                                    }
                                }
                                
                                public void isOwner() {
                                    if (!this.actual.isOwner()) {
                                        failWithActual(Fact.simpleFact("expected to be owner"));
                                    }
                                }

                                public void isNotOwner() {
                                    if (this.actual.isOwner()) {
                                        failWithActual(Fact.simpleFact("expected not to be owner"));
                                    }
                                }
                            }
                        """.trimIndent()

                )

                // Act & Assert
                assertAbout(javaSources())
                    .that(setOf(subject))
                    .withCompilerOptions("-Adebug")
                    .processedWith(AutoTruthProcessor())
                    .compilesWithoutError()
                    .and()
                    .generatesSources(generated)
            }
        }
    }
})
