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
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.ENUM
import javax.lang.model.element.ElementKind.ENUM_CONSTANT
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.ElementKind.PACKAGE
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter.fieldsIn
import javax.lang.model.util.ElementFilter.methodsIn
import javax.lang.model.util.ElementFilter.packagesIn

val Element.isPublic: Boolean
    get() = modifiers.contains(PUBLIC)

val Element.isStatic: Boolean
    get() = modifiers.contains(STATIC)

val ExecutableElement.hasParameter: Boolean
    get() = parameters.isNotEmpty()

val Element.isEnum: Boolean
    get() = kind == ENUM

fun Element.getPackage(): PackageElement {
    var enclosing = this
    while (enclosing.kind != PACKAGE) {
        enclosing = enclosing.enclosingElement
    }
    return packagesIn(setOf(enclosing)).first()
}

inline fun <reified T : Annotation> Element.getAnnotation(): T? {
    return getAnnotation(T::class.java)
}

inline fun <reified T : Annotation> Element.findAnnotationMirror(): AnnotationMirror? {
    val annotationName = T::class.java.canonicalName
    return annotationMirrors.firstOrNull {
        val annotationTypeElement = it.annotationType.asTypeElement()
        annotationTypeElement.qualifiedName.contentEquals(annotationName)
    }
}

fun TypeElement.findEnumConstants(predicate: (VariableElement) -> Boolean = { true }): List<VariableElement> {
    return fieldsIn(enclosedElements)
        .filter { it.kind == ENUM_CONSTANT }
        .filter(predicate)
}

fun TypeElement.findFields(predicate: (VariableElement) -> Boolean = { true }): List<VariableElement> {
    return fieldsIn(enclosedElements)
        .filter { it.kind == FIELD }
        .filter(predicate)
}

fun TypeElement.findMethods(predicate: (ExecutableElement) -> Boolean = { true }): List<ExecutableElement> {
    return methodsIn(enclosedElements)
        .filter(predicate)
}
