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

package io.t28.auto.truth.compiler.extensions

import com.google.common.truth.Truth.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object StringSpec : Spek({
    describe("safeFormat") {
        arrayOf(
            Fixture("Hello, world", "Hello, %s", arrayOf("world")),
            Fixture("Hello, %d", "Hello, %d", arrayOf("world")),
            Fixture("Hello", "Hello")
        ).forEach { (expected, format, args) ->
            it("should return $expected from format=$format and args=${args.joinToString(",")}") {
                // Act
                val actual = format.safeFormat(*args)

                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }
}) {
    @Suppress("ArrayInDataClass")
    data class Fixture(
        val expected: String,
        val format: String,
        val args: Array<Any> = emptyArray()
    )
}
