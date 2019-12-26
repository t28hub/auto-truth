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

import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

data class ExecutablePropertyElement(override val element: ExecutableElement) : PropertyElement<ExecutableElement> {
    companion object {
        private val GETTER_PREFIX = Regex("^(is|get)(.+?)$")
    }

    override val type: TypeMirror = element.returnType

    override val name: String = "${element.simpleName}"

    override val identifier: String = "$name()"

    override val simpleName: String
        get() {
            val matched = GETTER_PREFIX.find(name) ?: return name
            val candidate = matched.groups[1]?.value ?: return name
            if (candidate.first() !in 'A'..'Z') {
                return name
            }
            return candidate
        }
}
