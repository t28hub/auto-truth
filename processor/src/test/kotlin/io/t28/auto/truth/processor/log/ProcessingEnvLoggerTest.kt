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
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.NOTE
import javax.tools.Diagnostic.Kind.WARNING
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

internal class ProcessingEnvLoggerTest {
    @Mock
    lateinit var messager: Messager

    @Mock
    lateinit var element: Element

    @BeforeEach
    fun setUp() {
        initMocks(this)
    }

    @Nested
    @DisplayName("debug")
    inner class DebugTest {
        @Nested
        @DisplayName("debug is enabled")
        inner class DebugEnabledTest {
            @Test
            fun `should print message with NOTE`() {
                // Act
                val logger = ProcessingEnvLogger(messager, true)
                logger.debug("This is %s message", "debug")

                // Assert
                verify(messager).printMessage(eq(NOTE), eq("This is debug message"))
                verifyNoMoreInteractions(messager)
            }

            @Test
            fun `should print message with element`() {
                // Act
                val logger = ProcessingEnvLogger(messager, true)
                logger.debug(element, "This is %s message", "debug")

                // Assert
                verify(messager).printMessage(eq(NOTE), eq("This is debug message"), eq(element))
                verifyNoMoreInteractions(messager)
            }
        }

        @Nested
        @DisplayName("debug is disabled")
        inner class DebugDisabledTest {
            @Test
            fun `should not print message`() {
                // Act
                val logger = ProcessingEnvLogger(messager, false)
                logger.debug("This is %s message", "debug")

                // Assert
                verifyZeroInteractions(messager)
            }

            @Test
            fun `should not print message with element`() {
                // Act
                val logger = ProcessingEnvLogger(messager, false)
                logger.debug(element, "This is %s message", "debug")

                // Assert
                verifyZeroInteractions(messager)
            }
        }
    }

    @Nested
    inner class WarnTest {
        @Test
        fun `should print message with WARNING`() {
            // Act
            val logger = ProcessingEnvLogger(messager, true)
            logger.warn("This is %s message", "warning")

            // Assert
            verify(messager).printMessage(eq(WARNING), eq("This is warning message"))
            verifyNoMoreInteractions(messager)
        }

        @Test
        fun `should print message with element`() {
            // Act
            val logger = ProcessingEnvLogger(messager, true)
            logger.warn(element, "This is %s message", "warning")

            // Assert
            verify(messager).printMessage(eq(WARNING), eq("This is warning message"), eq(element))
            verifyNoMoreInteractions(messager)
        }
    }

    @Nested
    inner class ErrorTest {
        @Test
        fun `should print message with ERROR`() {
            // Act
            val logger = ProcessingEnvLogger(messager, true)
            logger.error("This is %s message", "error")

            // Assert
            verify(messager).printMessage(eq(ERROR), eq("This is error message"))
            verifyNoMoreInteractions(messager)
        }

        @Test
        fun `should print message with element`() {
            // Act
            val logger = ProcessingEnvLogger(messager, true)
            logger.error(element, "This is %s message", "error")

            // Assert
            verify(messager).printMessage(eq(ERROR), eq("This is error message"), eq(element))
            verifyNoMoreInteractions(messager)
        }
    }
}
