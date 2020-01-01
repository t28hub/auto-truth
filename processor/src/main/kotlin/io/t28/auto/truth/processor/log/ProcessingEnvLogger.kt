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

import io.t28.auto.truth.processor.extensions.safeFormat
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.NOTE
import javax.tools.Diagnostic.Kind.WARNING

class ProcessingEnvLogger(private val messager: Messager, private val debug: Boolean) : Logger {
    override fun debug(message: String, vararg args: Any?) {
        if (debug) {
            print(NOTE, message, *args)
        }
    }

    override fun debug(element: Element, message: String, vararg args: Any?) {
        if (debug) {
            print(NOTE, element, message, *args)
        }
    }

    override fun warn(message: String, vararg args: Any?) {
        print(WARNING, message, *args)
    }

    override fun warn(element: Element, message: String, vararg args: Any?) {
        print(WARNING, element, message, *args)
    }

    override fun error(message: String, vararg args: Any?) {
        print(ERROR, message, *args)
    }

    override fun error(element: Element, message: String, vararg args: Any?) {
        print(ERROR, element, message, *args)
    }

    private fun print(kind: Kind, message: String, vararg args: Any?) {
        messager.printMessage(kind, message.safeFormat(*args))
    }

    private fun print(kind: Kind, element: Element, message: String, vararg args: Any?) {
        messager.printMessage(kind, message.safeFormat(*args), element)
    }
}
