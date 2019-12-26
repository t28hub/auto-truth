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

import io.t28.auto.truth.compiler.extensions.asString
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.util.Elements

data class AutoSubjectAnnotation(
    private val annotation: AnnotationMirror,
    private val elementUtils: Elements
) {
    companion object {
        private const val FIELD_PREFIX = "prefix"
        private const val FIELD_SUFFIX = "suffix"
    }

    val prefix: String
        get() = findValue(FIELD_PREFIX).asString()

    val suffix: String
        get() = findValue(FIELD_SUFFIX).asString()

    private fun findValue(field: String): AnnotationValue {
        return elementUtils.getElementValuesWithDefaults(annotation)
            .filter { (key, _) -> key.simpleName.contentEquals(field) }
            .map { (_, value) -> value }
            .first()
    }
}
