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

package io.t28.auto.truth.compiler.log

import java.util.IllegalFormatException
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.NOTE
import javax.tools.Diagnostic.Kind.WARNING

class ProcessingEnvLogger(private val messager: Messager, private val debug: Boolean) : Logger {
    override fun debug(message: String, vararg args: Any) {
        if (debug) {
            messager.printMessage(NOTE, message.safeFormat(args))
        }
    }

    override fun debug(element: Element, message: String, vararg args: Any) {
        if (debug) {
            messager.printMessage(NOTE, message.safeFormat(args), element)
        }
    }

    override fun warn(message: String, vararg args: Any) {
        messager.printMessage(WARNING, message.format(args))
    }

    override fun warn(element: Element, message: String, vararg args: Any) {
        messager.printMessage(WARNING, message.format(args), element)
    }

    override fun error(message: String, vararg args: Any) {
        messager.printMessage(ERROR, message.format(args))
    }

    override fun error(element: Element, message: String, vararg args: Any) {
        messager.printMessage(ERROR, message.format(args), element)
    }

    private fun String.safeFormat(vararg args: Any): String {
        return try {
            format(args)
        } catch (e: IllegalFormatException) {
            this
        }
    }
}
