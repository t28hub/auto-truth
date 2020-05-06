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

import javax.lang.model.element.AnnotationValue
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor6
import kotlin.reflect.KClass

private open class ValueVisitor<T : Any>(private val type: KClass<T>) : SimpleAnnotationValueVisitor6<T, Void?>() {
    override fun defaultAction(value: Any, p: Void?): T {
        throw IllegalArgumentException("Cannot convert value($value) as ${type.simpleName}")
    }
}

private val STRING_VALUE_VISITOR = object : ValueVisitor<String>(String::class) {
    override fun visitString(value: String, p: Void?): String {
        return value
    }
}

private val TYPE_VALUE_VISITOR = object : ValueVisitor<TypeMirror>(TypeMirror::class) {
    override fun visitType(value: TypeMirror, p: Void?): TypeMirror {
        return value
    }
}

@Throws(IllegalArgumentException::class)
fun AnnotationValue.asString(): String {
    return STRING_VALUE_VISITOR.visit(this)
}

@Throws(IllegalArgumentException::class)
fun AnnotationValue.asType(): TypeMirror {
    return TYPE_VALUE_VISITOR.visit(this)
}
