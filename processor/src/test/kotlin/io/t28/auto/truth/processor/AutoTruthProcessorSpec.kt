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
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.util.fixture
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import javax.tools.StandardLocation.CLASS_OUTPUT
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun process(vararg resources: Resource): CompileTester {
    return process(*resources.map { it.toJavaFileObject() }.toTypedArray())
}

private fun process(vararg files: JavaFileObject): CompileTester {
    return assertAbout(javaSources())
        .that(files.toList())
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
                    process(Resource.User, Resource.ValidPrefixUserSubject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "Prefix\$ValidPrefixUserSubject.class")
                }

                it("should not append prefix when custom prefix is empty") {
                    // Act & Assert
                    process(Resource.User, Resource.EmptyPrefixUserSubject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "EmptyPrefixUserSubject$.class")
                }

                it("should compile with error when given prefix is invalid") {
                    // Act & Assert
                    process(Resource.User, Resource.InvalidPrefixUserSubject)
                        .failsToCompile()
                        .withErrorContaining("Prefix given within @AutoTruth is invalid: 1Prefix")
                }
            }

            describe("suffix") {
                it("should append suffix when custom suffix is given") {
                    // Act & Assert
                    process(Resource.User, Resource.ValidSuffixUserSubject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoValidSuffixUserSubject\$Suffix.class")
                }

                it("should not append suffix when custom suffix is empty") {
                    // Act & Assert
                    process(Resource.User, Resource.EmptySuffixUserSubject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, "io.t28.auto.truth.test", "AutoEmptySuffixUserSubject.class")
                }

                it("should compile with error when given suffix is invalid") {
                    // Act & Assert
                    process(Resource.User, Resource.InvalidSuffixUserSubject)
                        .failsToCompile()
                        .withErrorContaining("Suffix given within @AutoTruth is invalid: Suffix%")
                }
            }

            it("should generate Subject class") {
                // Act & Assert
                process(Resource.User, Resource.UserSubject, Resource.UserTypeSubject)
                    .compilesWithoutError()
                    .and()
                    .generatesSources(
                        Resource.AutoUserSubject.toJavaFileObject(),
                        Resource.AutoUserTypeSubject.toJavaFileObject()
                    )
            }

            arrayOf(
                fixture(Resource.PrimitiveTypes, Resource.PrimitiveTypesSubject, "AutoPrimitiveTypesSubject.class"),
                fixture(Resource.BoxedPrimitiveTypes, Resource.BoxedPrimitiveTypesSubject, "AutoBoxedPrimitiveTypesSubject.class"),
                fixture(Resource.ArrayTypes, Resource.ArrayTypesSubject, "AutoArrayTypesSubject.class"),
                fixture(Resource.IterableTypes, Resource.IterableTypesSubject, "AutoIterableTypesSubject.class"),
                fixture(Resource.MapTypes, Resource.MapTypesSubject, "AutoMapTypesSubject.class"),
                fixture(Resource.OptionalTypes, Resource.OptionalTypesSubject, "AutoOptionalTypesSubject.class"),
                fixture(Resource.StreamTypes, Resource.StreamTypesSubject, "AutoStreamTypesSubject.class"),
                fixture(Resource.EnumTypes, Resource.EnumTypesSubject, "AutoEnumTypesSubject.class"),
                fixture(Resource.GuavaTypes, Resource.GuavaTypesSubject, "AutoGuavaTypesSubject.class")
            ).forEach { (valueObject, subject, expected) ->
                it("should compile ${valueObject.simpleName} properties") {
                    // Act & Assert
                    process(valueObject, subject)
                        .compilesWithoutError()
                        .and()
                        .generatesFileNamed(CLASS_OUTPUT, subject.packageName, expected)
                }
            }
        }
    }
})
