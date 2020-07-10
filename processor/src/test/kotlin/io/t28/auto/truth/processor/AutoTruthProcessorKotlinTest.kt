/*
 * Copyright 2020 Tatsuya Maki
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

import com.google.common.truth.Truth.assertThat
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.KotlinCompilation.Result
import com.tschuchort.compiletesting.SourceFile.Companion.kotlin
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

internal class AutoTruthProcessorKotlinTest {
    @Test
    fun `should compile class`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            class Customer(val customerName: String)
            
            @AutoSubject(Customer::class)
            class CustomerSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile nested class`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            class Outer {
              private val bar: Int = 1
              class Nested {
                fun foo() = 2
              }
            }
            
            @AutoSubject(Outer.Nested::class)
            class NestedSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile interface`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            interface User {
              fun getName(): String
            }
            
            @AutoSubject(User::class)
            class UserSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile abstract class`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            abstract class User {
                abstract fun getName(): String
            }
            
            @AutoSubject(User::class)
            class UserSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile data class`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            data class User(val name: String, val age: Int)
            
            @AutoSubject(User::class)
            class UserSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile sealed class`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            sealed class Expr
            data class Const(val number: Double) : Expr()
            
            @AutoSubject(Expr::class)
            class ExprSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile enum class`() {
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            enum class Direction { 
                NORTH, SOUTH, WEST, EAST
            }
            
            @AutoSubject(Direction::class)
            class DirectionSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile object`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            sealed class Expr
            object NotANumber : Expr()
            
            @AutoSubject(NotANumber::class)
            class NotANumberSubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.OK)
    }

    @Test
    fun `should compile companion object with error`() {
        // Act
        @Language("kotlin")
        val result = compile("""
            package io.t28.auto.truth.test
            
            import io.t28.auto.truth.AutoSubject
            
            interface Factory<T> {
                fun create(): T
            }

            class MyClass { 
                companion object : Factory<MyClass> {
                    override fun create(): MyClass = MyClass() 
                }
            }
            
            @AutoSubject(MyClass.Factory::class)
            class FactorySubject
        """.trimIndent())

        // Assert
        assertThat(result.exitCode).isEqualTo(ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("An annotation argument must be a compile-time constant")
    }

    private fun compile(source: String): Result {
        return KotlinCompilation().apply {
            sources = listOf(kotlin("main.kt", source))
            annotationProcessors = listOf(AutoTruthProcessor())
            inheritClassPath = true
            messageOutputStream = System.out
        }.compile()
    }
}
