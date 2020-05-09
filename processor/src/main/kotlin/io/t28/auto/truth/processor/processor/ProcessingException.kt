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

package io.t28.auto.truth.processor.processor

import io.t28.auto.truth.processor.extensions.safeFormat
import javax.lang.model.element.Element

class ProcessingException(val element: Element, override val message: String) : Exception(message) {
    constructor(element: Element, message: String, vararg args: Any?) : this(element, message.safeFormat(*args))
}
