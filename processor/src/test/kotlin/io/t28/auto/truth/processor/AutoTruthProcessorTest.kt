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
import java.util.stream.Stream
import javax.lang.model.SourceVersion
import javax.tools.JavaFileObject
import javax.tools.StandardLocation.CLASS_OUTPUT
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

internal class AutoTruthProcessorTest {
    @Test
    fun `getSupportedOptions should return supported options`() {
        // Act
        val processor = AutoTruthProcessor()
        val actual = processor.supportedOptions

        // Assert
        assertThat(actual).apply {
            hasSize(1)
            contains("debug")
        }
    }

    @Test
    fun `getSupportedSourceVersion should return latest source version`() {
        // Act
        val processor = AutoTruthProcessor()
        val actual = processor.supportedSourceVersion

        // Assert
        assertThat(actual)
            .isEqualTo(SourceVersion.latestSupported())
    }

    @Test
    fun `getSupportedAnnotationTypes should return a set of annotation types contains @AutoSubject`() {
        // Act
        val processor = AutoTruthProcessor()
        val actual = processor.supportedAnnotationTypes

        // Assert
        assertThat(actual)
            .containsExactly("io.t28.auto.truth.AutoSubject")
    }

    @Nested
    @DisplayName("@AutoSubject(prefix = )")
    inner class PrefixTest {
        @Test
        fun `should append prefix when custom prefix is given`() {
            // Act & Assert
            process(Resource.User, Resource.ValidPrefixUserSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(
                    CLASS_OUTPUT,
                    Resource.ValidPrefixUserSubject.packageName,
                    "Prefix\$ValidPrefixUserSubject.class"
                )
        }

        @Test
        fun `should not append prefix when custom prefix is empty`() {
            // Act & Assert
            process(Resource.User, Resource.EmptyPrefixUserSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(
                    CLASS_OUTPUT,
                    Resource.EmptyPrefixUserSubject.packageName,
                    "EmptyPrefixUserSubject$.class"
                )
        }

        @Test
        fun `should compile with error when given prefix is invalid`() {
            // Act & Assert
            process(Resource.User, Resource.InvalidPrefixUserSubject)
                .failsToCompile()
                .withErrorContaining("Prefix given within @AutoTruth is invalid: 1Prefix")
        }
    }

    @Nested
    @DisplayName("@AutoSubject(suffix = )")
    inner class SuffixTest {
        @Test
        fun `should append suffix when custom suffix is given`() {
            // Act & Assert
            process(Resource.User, Resource.ValidSuffixUserSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(
                    CLASS_OUTPUT,
                    Resource.ValidSuffixUserSubject.packageName,
                    "AutoValidSuffixUserSubject\$Suffix.class"
                )
        }

        @Test
        fun `should not append suffix when custom suffix is empty`() {
            // Act & Assert
            process(Resource.User, Resource.EmptySuffixUserSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(
                    CLASS_OUTPUT,
                    Resource.EmptySuffixUserSubject.packageName,
                    "AutoEmptySuffixUserSubject.class"
                )
        }

        @Test
        fun `should compile with error when given suffix is invalid`() {
            // Act & Assert
            process(Resource.User, Resource.InvalidSuffixUserSubject)
                .failsToCompile()
                .withErrorContaining("Suffix given within @AutoTruth is invalid: Suffix%")
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("@AutoSubject(value = )")
    inner class ValueTest {
        @Test
        fun `should compile abstract class`() {
            // Act & Assert
            process(Resource.AbstractClass, Resource.AbstractClassSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.AbstractClassSubject.packageName, "AutoAbstractClassSubject.class")
        }

        @Test
        fun `should compile enum class`() {
            // Act & Assert
            process(Resource.EnumType, Resource.EnumTypeSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.EnumTypeSubject.packageName, "AutoEnumTypeSubject.class")
        }

        @Test
        fun `should compile interface`() {
            // Act & Assert
            process(Resource.InterfaceType, Resource.InterfaceTypeSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.InterfaceTypeSubject.packageName, "AutoInterfaceTypeSubject.class")
        }

        @Test
        fun `should compile nested class`() {
            // Act & Assert
            process(Resource.NestedClass, Resource.NestedClassSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.NestedClassSubject.packageName, "AutoNestedClassSubject.class")
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.NestedClassSubject.packageName, "AutoNestedSubject.class")
        }

        @Test
        fun `should compile package private class`() {
            // Act & Assert
            process(Resource.PackagePrivateClass, Resource.PackagePrivateClassSubject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, Resource.NestedClassSubject.packageName, "AutoPackagePrivateClassSubject.class")
        }

        @Test
        fun `should generate Subject class`() {
            // Act & Assert
            process(Resource.User, Resource.UserSubject, Resource.UserTypeSubject)
                .compilesWithoutError()
                .and()
                .generatesSources(
                    Resource.AutoUserSubject.toJavaFileObject(),
                    Resource.AutoUserTypeSubject.toJavaFileObject()
                )
        }

        @ParameterizedTest(name = "should compile {0} and {1} and generate {2}")
        @MethodSource("provideSupportedTypes")
        fun `should compile supported types`(valueObject: Resource, subject: Resource, expected: String) {
            // Act & Assert
            process(valueObject, subject)
                .compilesWithoutError()
                .and()
                .generatesFileNamed(CLASS_OUTPUT, subject.packageName, expected)
        }

        @Suppress("unused")
        fun provideSupportedTypes(): Stream<Arguments> {
            return Stream.of(
//                of(Resource.PrimitiveTypes, Resource.PrimitiveTypesSubject, "AutoPrimitiveTypesSubject.class"),
//                of(Resource.BoxedPrimitiveTypes, Resource.BoxedPrimitiveTypesSubject, "AutoBoxedPrimitiveTypesSubject.class"),
//                of(Resource.ArrayTypes, Resource.ArrayTypesSubject, "AutoArrayTypesSubject.class"),
//                of(Resource.IterableTypes, Resource.IterableTypesSubject, "AutoIterableTypesSubject.class"),
//                of(Resource.MapTypes, Resource.MapTypesSubject, "AutoMapTypesSubject.class"),
//                of(Resource.ClassTypes, Resource.ClassTypesSubject, "AutoClassTypesSubject.class"),
//                of(Resource.OptionalTypes, Resource.OptionalTypesSubject, "AutoOptionalTypesSubject.class"),
//                of(Resource.StreamTypes, Resource.StreamTypesSubject, "AutoStreamTypesSubject.class"),
//                of(Resource.PathTypes, Resource.PathTypesSubject, "AutoPathTypesSubject.class"),
//                of(Resource.EnumTypes, Resource.EnumTypesSubject, "AutoEnumTypesSubject.class"),
//                of(Resource.GuavaTypes, Resource.GuavaTypesSubject, "AutoGuavaTypesSubject.class"),
                of(Resource.GenericTypes, Resource.GenericTypesSubject, "AutoGenericTypesSubject.class")
            )
        }
    }

    companion object {
        private fun process(vararg resources: Resource): CompileTester {
            return process(*resources.map { it.toJavaFileObject() }.toTypedArray())
        }

        private fun process(vararg files: JavaFileObject): CompileTester {
            return assertAbout(javaSources())
                .that(files.toList())
                .withCompilerOptions("-Adebug")
                .processedWith(AutoTruthProcessor())
        }
    }
}
