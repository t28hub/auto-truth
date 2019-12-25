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
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement
import javax.lang.model.util.ElementFilter

val Element.isPublic: Boolean
    get() = this.modifiers.contains(Modifier.PUBLIC)

val Element.isStatic: Boolean
    get() = this.modifiers.contains(Modifier.STATIC)

val ExecutableElement.hasParameter: Boolean
    get() = this.parameters.isNotEmpty()

fun Element.getPackage(): PackageElement {
    var enclosing = this
    while (enclosing.kind != ElementKind.PACKAGE) {
        enclosing = enclosing.enclosingElement
    }
    return ElementFilter.packagesIn(setOf(enclosing)).first()
}
