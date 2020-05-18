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
import io.t28.auto.truth.processor.util.fixture
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object StringSpec : Spek({
    describe("isValidClassPrefix") {
        arrayOf(
            fixture("", true),
            fixture("$", true),
            fixture("_", true),
            fixture("1", false),
            fixture("@", false),
            fixture("Prefix", true),
            fixture("Pref1x", true),
            fixture("Prefix$", true),
            fixture("Prefix@", false)
        ).forEach { (string, expected) ->
            it("should return $expected when prefix is $string") {
                // Act
                val actual = string.isValidClassPrefix()

                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }

    describe("isValidClassSuffix") {
        arrayOf(
            fixture("", true),
            fixture("$", true),
            fixture("_", true),
            fixture("1", false),
            fixture("@", false),
            fixture("Suffix", true),
            fixture("Suff1x", true),
            fixture("\$uffix", true),
            fixture("Suffix@", false)
        ).forEach { (string, expected) ->
            it("should return $expected when suffix is $string") {
                // Act
                val actual = string.isValidClassPrefix()

                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }

    describe("safeFormat") {
        arrayOf(
            fixture("Hello, world", "Hello, %s", arrayOf("world")),
            fixture("Hello, %d", "Hello, %d", arrayOf("world")),
            fixture("Hello", "Hello", emptyArray())
        ).forEach { (expected, format, args) ->
            it("should return $expected from format=$format and args=${args.joinToString(",")}") {
                // Act
                val actual = format.safeFormat(*args)

                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }
})
