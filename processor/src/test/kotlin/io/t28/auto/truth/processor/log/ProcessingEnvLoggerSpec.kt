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

package io.t28.auto.truth.processor.log

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.NOTE
import javax.tools.Diagnostic.Kind.WARNING
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ProcessingEnvLoggerSpec : Spek({
    describe("ProcessingEnvLogger") {
        val messager = mock<Messager>()
        val element = mock<Element>()
        beforeEachTest {
            reset(messager, element)
        }

        describe("debug") {
            describe("debug is true") {
                val logger = ProcessingEnvLogger(messager, true)

                it("should print message with NOTE") {
                    // Act
                    logger.debug("This is %s message", "debug")

                    // Assert
                    verify(messager).printMessage(eq(NOTE), eq("This is debug message"))
                    verifyNoMoreInteractions(messager)
                }

                it("should print message with element") {
                    // Act
                    logger.debug(element, "This is %s message", "debug")

                    // Assert
                    verify(messager).printMessage(eq(NOTE), eq("This is debug message"), eq(element))
                    verifyNoMoreInteractions(messager)
                }
            }

            describe("debug is false") {
                val logger = ProcessingEnvLogger(messager, false)

                it("should not print message") {
                    // Act
                    logger.debug("This is %s message", "debug")

                    // Assert
                    verifyZeroInteractions(messager)
                }

                it("should not print message with element") {
                    // Act
                    logger.debug(element, "This is %s message", "debug")

                    // Assert
                    verifyZeroInteractions(messager)
                }
            }

            describe("warn") {
                val logger = ProcessingEnvLogger(messager, true)

                it("should print message with WARNING") {
                    // Act
                    logger.warn("This is %s message", "warning")

                    // Assert
                    verify(messager).printMessage(eq(WARNING), eq("This is warning message"))
                    verifyNoMoreInteractions(messager)
                }

                it("should print message with element") {
                    // Act
                    logger.warn(element, "This is %s message", "warning")

                    // Assert
                    verify(messager).printMessage(eq(WARNING), eq("This is warning message"), eq(element))
                    verifyNoMoreInteractions(messager)
                }
            }

            describe("error") {
                val logger = ProcessingEnvLogger(messager, true)

                it("should print message with ERROR") {
                    // Act
                    logger.error("This is %s message", "error")

                    // Assert
                    verify(messager).printMessage(eq(ERROR), eq("This is error message"))
                    verifyNoMoreInteractions(messager)
                }

                it("should print message with element") {
                    // Act
                    logger.error(element, "This is %s message", "error")

                    // Assert
                    verify(messager).printMessage(eq(ERROR), eq("This is error message"), eq(element))
                    verifyNoMoreInteractions(messager)
                }
            }
        }
    }
})
