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

package io.t28.auto.truth.processor.extensions

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.util.ElementFilter.methodsIn

fun AnnotationMirror.getAnnotationValue(name: String): AnnotationValue? {
    val method = methodsIn(annotationType.asTypeElement().enclosedElements)
        .firstOrNull {
            it.simpleName.contentEquals(name)
        }

    return when {
        // Use declared value at first
        elementValues.containsKey(method) -> {
            elementValues[method]
        }
        // Use default value if exists at second
        method?.defaultValue != null -> {
            method.defaultValue
        }
        else -> {
            null
        }
    }
}
