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
import io.t28.auto.truth.processor.extensions.findEnumConstants
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.testing.MethodSpecSubject.Companion.assertThat
import io.t28.auto.truth.processor.testing.TestContext
import io.t28.auto.truth.processor.testing.TestProcessor
import javax.lang.model.element.Modifier.PUBLIC
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun compile(handler: (TestContext) -> Unit): CompileTester {
    return assertAbout(javaSources())
        .that(listOf(
            forSourceString("io.t28.auto.truth.test.EnumTypeSubject", """
                package io.t28.auto.truth.test;
            
                import io.t28.auto.truth.AutoSubject;
            
                @AutoSubject(value = EnumType.class)
                public class EnumTypeSubject {
                }
            """.trimIndent()),
            forSourceString("io.t28.auto.truth.test.EnumType", """
                package io.t28.auto.truth.test;
                
                public enum EnumType {
                    FOO_BAR,
                    BAR_BAZ,
                    BAX_QUX;
                }
            """.trimIndent())
        ))
        .processedWith(TestProcessor.builder()
            .annotations(AutoSubject::class)
            .nextHandler { context ->
                handler(context)
                true
            }
            .build())
}

object EnumAssertionGeneratorSpec : Spek({
    describe("EnumAssertionGenerator") {
        describe("matches") {
            it("should return true when given type is EnumConstant") {
                compile {
                    // Arrange
                    val element = it.getTypeElement("io.t28.auto.truth.test.EnumType")
                    val property = element.findEnumConstants { constant ->
                        constant.simpleName.contentEquals("FOO_BAR")
                    }.map { constant -> Property.get(constant) }.first()
                    val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.matches(property)

                    // Assert
                    assertThat(actual).isTrue()
                }.compilesWithoutError()
            }

            it("should return false when given type is not EnumConstant") {
                compile {
                    // Arrange
                    val element = it.getTypeElement("io.t28.auto.truth.test.EnumType")
                    val property = element.findMethods { method ->
                        method.simpleName.contentEquals("values")
                    }.map { method -> Property.get(method) }.first()
                    val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.matches(property)

                    // Assert
                    assertThat(actual).isFalse()
                }.compilesWithoutError()
            }
        }

        describe("generate") {
            it("should generate positive assertion method") {
                compile {
                    // Arrange
                    val element = it.getTypeElement("io.t28.auto.truth.test.EnumType")
                    val property = element.findEnumConstants { constant ->
                        constant.simpleName.contentEquals("FOO_BAR")
                    }.map { constant -> Property.get(constant) }.first()
                    val generator = EnumAssertionGenerator.PositiveAssertionGenerator(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.generate(property)

                    // Assert
                    assertThat(actual).apply {
                        hasName("isFooBar")
                        modifiers().contains(PUBLIC)
                        hasReturnType(TypeName.VOID)
                        parameters().isEmpty()
                    }
                }.compilesWithoutError()
            }

            it("should generate negative assertion method") {
                compile {
                    // Arrange
                    val element = it.getTypeElement("io.t28.auto.truth.test.EnumType")
                    val property = element.findEnumConstants { constant ->
                        constant.simpleName.contentEquals("FOO_BAR")
                    }.map { constant -> Property.get(constant) }.first()
                    val generator = EnumAssertionGenerator.NegativeAssertionGenerator(Context.get(it.processingEnv))

                    // Act
                    val actual = generator.generate(property)

                    // Assert
                    assertThat(actual).apply {
                        hasName("isNotFooBar")
                        modifiers().contains(PUBLIC)
                        hasReturnType(TypeName.VOID)
                        parameters().isEmpty()
                    }
                }.compilesWithoutError()
            }
        }
    }
})
