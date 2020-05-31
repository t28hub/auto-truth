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

package io.t28.auto.truth.processor.generator

import com.google.common.truth.Subject
import com.google.common.truth.Truth
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects.forResource
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.squareup.javapoet.ClassName
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.data.ValueObjectClass
import io.t28.auto.truth.processor.testing.TestContext
import io.t28.auto.truth.processor.testing.TestProcessor
import io.t28.auto.truth.processor.testing.TypeSpecSubject.Companion.assertThat
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PROTECTED
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private fun process(handler: (TestContext) -> Unit): CompileTester {
    return Truth.assertAbout(javaSources())
        .that(listOf(
            forResource("io/t28/auto/truth/test/User.java"),
            forResource("io/t28/auto/truth/test/UserSubject.java")
        ))
        .processedWith(TestProcessor.builder()
            .annotations(AutoSubject::class)
            .nextHandler { context ->
                handler(context)
                true
            }
            .build())
}

object SubjectClassGeneratorSpec : Spek({
    describe("SubjectClassGenerator") {
        val generator = SubjectClassGenerator()
        describe("generate") {
            it("should return Subject class specification") {
                process { context ->
                    // Arrange
                    val input = SubjectClass(
                        packageName = "io.t28.auto.truth.test",
                        prefix = "Auto",
                        suffix = "",
                        element = context.getTypeElement("io.t28.auto.truth.test.UserSubject"),
                        valueObject = ValueObjectClass(
                            element = context.getTypeElement("io.t28.auto.truth.test.User")
                        )
                    )

                    // Act
                    val actual = generator.generate(input)

                    // Assert
                    assertThat(actual).apply {
                        hasName("AutoUserSubject")
                        modifiers().containsExactly(PUBLIC)
                        hasSuperclass(Subject::class)
                        fieldSpec { field -> field.name == "actual" }.apply {
                            hasType(ClassName.get(input.valueObject.element))
                            hasName("actual")
                            modifiers().containsExactly(PRIVATE, FINAL)
                        }
                        methodSpec { method -> method.isConstructor }.apply {
                            modifiers().containsExactly(PROTECTED)
                            parameters().hasSize(2)
                        }
                        methodSpec { method -> method.name == "assertThat" }.apply {
                            modifiers().containsExactly(PUBLIC, STATIC)
                            parameters().hasSize(1)
                        }
                        methodSpec { method -> method.name == "user" }.apply {
                            modifiers().containsExactly(PUBLIC, STATIC)
                            parameters().isEmpty()
                        }
                    }
                }.compilesWithoutError()
            }
        }
    }
})
