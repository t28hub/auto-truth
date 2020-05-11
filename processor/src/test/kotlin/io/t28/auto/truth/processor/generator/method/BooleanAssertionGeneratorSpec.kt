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

package io.t28.auto.truth.processor.generator.method

import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects.forSourceString
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.testing.MethodSpecSubject.Companion.assertThat
import io.t28.auto.truth.processor.testing.TestContext
import io.t28.auto.truth.processor.testing.TestProcessor
import java.lang.reflect.Modifier.PUBLIC
import javax.lang.model.type.TypeKind.BOOLEAN
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun compile(handler: (TestContext) -> Unit): CompileTester {
    return assertAbout(javaSources())
        .that(listOf(forSourceString("io.t28.auto.truth.test.Dummy", """
            package io.t28.auto.truth.test;
            
            import io.t28.auto.truth.AutoSubject;
            
            @AutoSubject(value = String.class)
            public class Dummy {
            }
        """.trimIndent())))
        .processedWith(TestProcessor.builder()
            .annotations(AutoSubject::class)
            .nextHandler { context ->
                handler(context)
                true
            }
            .build())
}

object BooleanAssertionGeneratorSpec : Spek({
    describe("BooleanAssertionGenerator") {
        describe("matches") {
            it("should return true when type is boolean") {
                compile {
                    // Arrange
                    val typeMirror = it.getPrimitiveType(BOOLEAN)
                    val generator = BooleanAssertionGenerator.positiveAssertion(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.matches(typeMirror)

                    // Assert
                    assertThat(actual).isTrue()
                }.compilesWithoutError()
            }

            it("should return true when type is boxed boolean") {
                compile {
                    // Arrange
                    val type = it.getDeclaredType("java.lang.Boolean")
                    val generator = BooleanAssertionGenerator.positiveAssertion(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.matches(type)

                    // Assert
                    assertThat(actual).isTrue()
                }.compilesWithoutError()
            }

            it("should return false when type is not boolean and boxed boolean") {
                compile {
                    // Arrange
                    val type = it.getDeclaredType("java.lang.Object")
                    val generator = BooleanAssertionGenerator.positiveAssertion(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.matches(type)

                    // Assert
                    assertThat(actual).isFalse()
                }.compilesWithoutError()
            }
        }

        describe("generate") {
            it("should generate positive assertion method") {
                compile {
                    // Arrange
                    val type = it.getPrimitiveType(BOOLEAN)
                    val generator = BooleanAssertionGenerator.positiveAssertion(Context.get(it.processingEnv))
                    val property = Property(
                        element = it.getTypeElement("io.t28.auto.truth.test.Dummy"),
                        type = type,
                        name = "booleanValue",
                        symbol = "booleanValue()"
                    )

                    // Act
                    val actual = generator.generate(property)

                    // Assert
                    assertThat(actual).apply {
                        hasName("isBooleanValue")
                        modifiers().contains(PUBLIC)
                        hasReturnType(TypeName.VOID)
                        parameters().isEmpty()
                    }
                }
            }

            it("should generate negative assertion method") {
                compile {
                    // Arrange
                    val type = it.getPrimitiveType(BOOLEAN)
                    val generator = BooleanAssertionGenerator.positiveAssertion(Context.get(it.processingEnv))
                    val property = Property(
                        element = it.getTypeElement("io.t28.auto.truth.test.Dummy"),
                        type = type,
                        name = "booleanValue",
                        symbol = "booleanValue()"
                    )

                    // Act
                    val actual = generator.generate(property)

                    // Assert
                    assertThat(actual).apply {
                        hasName("isNotBooleanValue")
                        modifiers().contains(PUBLIC)
                        hasReturnType(TypeName.VOID)
                        parameters().isEmpty()
                    }
                }
            }
        }
    }
})
