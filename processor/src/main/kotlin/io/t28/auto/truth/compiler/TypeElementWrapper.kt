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

package io.t28.auto.truth.compiler

import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class TypeElementWrapper(element: TypeElement) : ElementWrapper<TypeElement>(element) {
    val qualifiedName: String
        get() = "${element.qualifiedName}"

    val type: TypeWrapper
        get() = TypeWrapper.wrap(element.asType())

    override val name: String
        get() = qualifiedName.removePrefix("${packageName}.")

    private val fields: Collection<VariableElementWrapper>
        get() = ElementFilter.fieldsIn(element.enclosedElements)
                .map { element -> wrap(element) }

    private val methods: Collection<ExecutableElementWrapper>
        get() = ElementFilter.methodsIn(element.enclosedElements)
                .map { element -> wrap(element) }

    fun findFields(predicate: (VariableElementWrapper) -> Boolean): Collection<VariableElementWrapper> {
        return fields.filter(predicate)
    }

    fun findMethods(predicate: (ExecutableElementWrapper) -> Boolean): Collection<ExecutableElementWrapper> {
        return methods.filter(predicate)
    }
}
