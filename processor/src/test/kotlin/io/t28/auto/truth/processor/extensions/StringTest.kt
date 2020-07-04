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

package io.t28.auto.truth.processor.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

internal class StringTest {
    @ParameterizedTest(name = "should return {1} when prefix is {0}")
    @MethodSource("providePrefixes")
    fun `should return whether prefix is valid`(string: String, expected: Boolean) {
        // Act
        val actual = string.isValidClassPrefix()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "should return {1} when suffix is {0}")
    @MethodSource("provideSuffixes")
    fun `should return whether suffix is valid`(string: String, expected: Boolean) {
        // Act
        val actual = string.isValidClassSuffix()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest(name = "should return {2} when format is {0} and args are {1}")
    @MethodSource("provideFormats")
    fun `should format string safely`(format: String, args: Array<String>, expected: String) {
        // Act
        val actual = format.safeFormat(*args)

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        fun providePrefixes(): Array<Arguments> {
            return arrayOf(
                of("", true),
                of("$", true),
                of("_", true),
                of("1", false),
                of("@", false),
                of("Prefix", true),
                of("Pref1x", true),
                of("Prefix$", true),
                of("Prefix@", false)
            )
        }

        @JvmStatic
        fun provideSuffixes(): Array<Arguments> {
            return arrayOf(
                of("", true),
                of("$", true),
                of("_", true),
                of("1", true),
                of("@", false),
                of("Suffix", true),
                of("Suff1x", true),
                of("\$uffix", true),
                of("Suffix@", false)
            )
        }

        @JvmStatic
        fun provideFormats(): Array<Arguments> {
            return arrayOf(
                of("Hello, %s", arrayOf("world"), "Hello, world"),
                of("Hello, %d", arrayOf("world"), "Hello, %d"),
                of("Hello", arrayOf("world"), "Hello"),
                of("Hello", emptyArray<String>(), "Hello")
            )
        }
    }
}
