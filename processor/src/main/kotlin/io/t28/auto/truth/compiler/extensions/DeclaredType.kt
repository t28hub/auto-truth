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

package io.t28.auto.truth.compiler.extensions

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.SimpleElementVisitor8

fun DeclaredType.asTypeElement(): TypeElement {
    return asElement().accept(object : SimpleElementVisitor8<TypeElement, Unit>() {
        override fun visitType(element: TypeElement, parameter: Unit): TypeElement {
            return element
        }

        override fun defaultAction(element: Element, parameter: Unit): TypeElement {
            throw IllegalStateException("Element cannot be cast as TypeElement: $element")
        }
    }, Unit)
}
