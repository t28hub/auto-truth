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

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.VOID
import io.t28.auto.truth.compiler.extensions.getPackage
import io.t28.auto.truth.compiler.extensions.hasParameter
import io.t28.auto.truth.compiler.extensions.isPublic
import io.t28.auto.truth.compiler.extensions.isStatic
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter.fieldsIn
import javax.lang.model.util.ElementFilter.methodsIn

data class AnnotatedTypeElement(private val element: TypeElement) {
    companion object {
        private val BOXED_VOID = ClassName.get(Void::class.java)
    }

    val packageName: String
        get() = "${element.getPackage().qualifiedName}"

    val type: TypeMirror
        get() = element.asType()

    val name: String
        get() = "${element.qualifiedName}".removePrefix("$packageName.")

    fun properties(): Collection<PropertyElement<out Element>> {
        // Find public and non-static properties from fields
        val fieldProperties = findFields { field ->
            field.isPublic and !field.isStatic
        }.map(PropertyElement.Companion::get)

        // Find public and non-static getter methods from methods
        val getterProperties = findMethods { method ->
            method.isPublic and !method.isStatic and !method.hasParameter
        }
            .filterNot { method ->
                val type = TypeName.get(method.returnType)
                (type == VOID) or (type == BOXED_VOID)
            }
            .map(PropertyElement.Companion::get)
        return fieldProperties + getterProperties
    }

    private fun findFields(predicate: (VariableElement) -> Boolean): Collection<VariableElement> {
        return fieldsIn(element.enclosedElements)
            .filter { field -> field.kind == FIELD } // Exclude ElementKind.ENUM_CONSTANT
            .filter(predicate)
    }

    private fun findMethods(predicate: (ExecutableElement) -> Boolean): Collection<ExecutableElement> {
        return methodsIn(element.enclosedElements)
            .filter(predicate)
    }
}
