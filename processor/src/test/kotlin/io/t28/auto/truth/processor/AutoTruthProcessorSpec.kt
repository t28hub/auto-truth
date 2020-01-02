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
import com.google.testing.compile.JavaFileObjects.forSourceString
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import javax.tools.StandardLocation.CLASS_OUTPUT
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun String.readResource(path: String = "io/t28/auto/truth/test"): JavaFileObject {
    return forResource("$path/$this")
}

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
            listOf(
                "User.java" to "AutoUserSubject.java",
                "VoidValueObject.java" to "AutoVoidValueObjectSubject.java",
                "BooleanValueObject.java" to "AutoBooleanValueObjectSubject.java",
                "PrimitiveValueObject.java" to "AutoPrimitiveValueObjectSubject.java",
                "PrimitiveArrayValueObject.java" to "AutoPrimitiveArrayValueObjectSubject.java",
                "IterableValueObject.java" to "AutoIterableValueObjectSubject.java",
                "MapValueObject.java" to "AutoMapValueObjectSubject.java",
                "ObjectArrayValueObject.java" to "AutoObjectArrayValueObjectSubject.java"
            ).forEach { (value, expected) ->
                it("should generate Subject class for $value") {
                    // Arrange
                    val valueJavaFile = value.readResource()
                    val expectedJavaFile = expected.readResource()

                    // Act & Assert
                    assertAbout(javaSources())
                        .that(setOf(valueJavaFile))
                        .withCompilerOptions("-Adebug")
                        .processedWith(AutoTruthProcessor())
                        .compilesWithoutError()
                        .and()
                        .generatesSources(expectedJavaFile)
                }
            }
        }
    }
})
