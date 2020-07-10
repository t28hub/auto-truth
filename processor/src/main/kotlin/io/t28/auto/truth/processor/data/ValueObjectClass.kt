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

package io.t28.auto.truth.processor.data

import io.t28.auto.truth.processor.extensions.findEnumConstants
import io.t28.auto.truth.processor.extensions.findFields
import io.t28.auto.truth.processor.extensions.findMethods
import io.t28.auto.truth.processor.extensions.getAnnotation
import io.t28.auto.truth.processor.extensions.hasParameter
import io.t28.auto.truth.processor.extensions.isPublic
import io.t28.auto.truth.processor.extensions.isStatic
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

data class ValueObjectClass(val element: TypeElement) {
    val type: TypeMirror
        get() = element.asType()

    val simpleName: String
        get() = "${element.simpleName}"

    fun findProperties(): List<Property> {
        // Find public and non-static properties
        val fieldProperties = element.findFields {
            it.isPublic and !it.isStatic
        }.map { Property.get(it) }

        // Find public and non-static getter methods
        val getterProperties = element
            .findMethods { method ->
                method.isPublic and !method.isStatic and !method.hasParameter
            }
            .filterNot { method ->
                // Ignore component functions
                if (isKotlinClass()) {
                    method.simpleName.matches("""component\d+""".toRegex())
                } else {
                    false
                }
            }
            .map { Property.get(it) }
        return fieldProperties + getterProperties
    }

    fun findEnumConstants(): List<Property> {
        return element.findEnumConstants()
            .map { Property.get(it) }
    }

    private fun isKotlinClass(): Boolean {
        return element.getAnnotation<Metadata>() != null
    }
}
