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

package io.t28.auto.truth.processor.data

import com.google.common.base.CaseFormat
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.ENUM_CONSTANT
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleElementVisitor8

sealed class Property(open val element: Element) {
    abstract val type: TypeMirror

    abstract val name: String

    abstract val symbol: String

    companion object {
        private val GETTER_PREFIX = Regex("^(is|get)(.+?)$")

        fun get(element: Element): Property {
            return object : SimpleElementVisitor8<Property, Void?>() {
                override fun visitExecutable(e: ExecutableElement, p: Void?): Property {
                    return when (e.kind) {
                        METHOD -> Getter(e)
                        else -> defaultAction(e, p)
                    }
                }

                override fun visitVariable(e: VariableElement, p: Void?): Property {
                    return when (e.kind) {
                        FIELD -> Field(e)
                        ENUM_CONSTANT -> EnumConstant(e)
                        else -> defaultAction(e, p)
                    }
                }

                override fun defaultAction(e: Element, p: Void?): Property {
                    throw IllegalArgumentException("Unsupported element type: ${e.kind}")
                }
            }.visit(element)
        }

        internal fun String.simplify(): String {
            val matched = GETTER_PREFIX.find(this) ?: return this
            val candidate = matched.groups[2]?.value ?: return this
            if (candidate.first() !in 'A'..'Z') {
                return this
            }
            return candidate.decapitalize()
        }
    }

    data class Field internal constructor(override val element: VariableElement) : Property(element) {
        override val type: TypeMirror
            get() = element.asType()

        override val name: String
            get() = "${element.simpleName}".simplify()

        override val symbol: String
            get() = "${element.simpleName}"
    }

    data class Getter internal constructor(override val element: ExecutableElement) : Property(element) {
        override val type: TypeMirror
            get() = element.returnType

        override val name: String
            get() = "${element.simpleName}".simplify()

        override val symbol: String
            get() = "${element.simpleName}()"
    }

    data class EnumConstant internal constructor(override val element: VariableElement) : Property(element) {
        override val type: TypeMirror
            get() = element.asType()

        override val name: String
            get() {
                val simpleName = "${element.simpleName}"
                return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, simpleName)
            }

        override val symbol: String
            get() = "${element.simpleName}"
    }
}
