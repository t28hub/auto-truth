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

import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind.NULL
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleTypeVisitor8

val TypeMirror.isNull: Boolean
    get() = kind == NULL

fun TypeMirror.isBoxedPrimitive(): Boolean {
    return accept(object : SimpleTypeVisitor8<Boolean, Void>() {
        override fun visitDeclared(type: DeclaredType, p: Void?): Boolean {
            val typeElement = type.asTypeElement()
            return when ("${typeElement.qualifiedName}") {
                "java.lang.Boolean" -> true
                "java.lang.Byte" -> true
                "java.lang.Character" -> true
                "java.lang.Short" -> true
                "java.lang.Integer" -> true
                "java.lang.Long" -> true
                "java.lang.Float" -> true
                "java.lang.Double" -> true
                else -> false
            }
        }

        override fun defaultAction(type: TypeMirror, p: Void?): Boolean {
            return false
        }
    }, null)
}

fun DeclaredType.asTypeElement(): TypeElement {
    // Use cast instead of ElementVisitor, since DeclaredType corresponds to TypeElement
    return asElement() as TypeElement
}
