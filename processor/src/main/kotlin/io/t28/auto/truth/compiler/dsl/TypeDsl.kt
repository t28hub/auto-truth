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

package io.t28.auto.truth.compiler.dsl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

class TypeDsl private constructor(private val builder: TypeSpec.Builder) {
    companion object {
        fun `class`(name: String): TypeDsl = TypeDsl(TypeSpec.classBuilder(name))

        fun `class`(name: ClassName): TypeDsl = TypeDsl(TypeSpec.classBuilder(name))
    }

    infix fun extends(superclass: KClass<*>) {
        builder.superclass(superclass.java)
    }

    @Suppress("unused", "FunctionName")
    fun `@`(type: KClass<out Annotation>, init: AnnotationDsl.() -> Unit = {}) {
        annotation(type, init)
    }

    fun annotation(type: KClass<out Annotation>, init: AnnotationDsl.() -> Unit = {}) {
        val annotation = AnnotationDsl(type.java).apply(init).build()
        builder.addAnnotation(annotation)
    }

    fun modifiers(vararg modifiers: Modifier) {
        builder.addModifiers(*modifiers)
    }

    fun typeParams(vararg params: TypeVariableName) {
        builder.addTypeVariables(params.asIterable())
    }

    fun field(type: TypeName, name: String, init: FieldDsl.() -> Unit = {}) {
        val fieldSpec = FieldDsl(type, name).apply(init).build()
        builder.addField(fieldSpec)
    }

    fun constructor(vararg params: ParameterSpec, init: MethodDsl.() -> Unit) {
        val methodSpec = MethodDsl.constructor().apply { params(*params) }.apply(init).build()
        builder.addMethod(methodSpec)
    }

    fun method(name: String, vararg params: ParameterSpec, init: MethodDsl.() -> Unit) {
        val methodSpec = MethodDsl.method(name).apply { params(*params) }.apply(init).build()
        builder.addMethod(methodSpec)
    }

    fun method(method: MethodSpec) {
        builder.addMethod(method)
    }

    fun build(): TypeSpec {
        return builder.build()
    }
}
