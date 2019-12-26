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

package io.t28.auto.truth.compiler.element

import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

data class VariablePropertyElement(override val element: VariableElement) : PropertyElement<VariableElement> {
    override val type: TypeMirror = element.asType()

    override val name: String = "${element.simpleName}"

    override val identifier: String = name

    override val simpleName: String
        get() {
            if (!name.startsWith("is") or (name == "is")) {
                return name
            }

            val candidate = name.removePrefix("is")
            if (candidate.first() !in 'A'..'Z') {
                return name
            }
            return candidate
        }
}
