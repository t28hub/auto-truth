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
import java.lang.reflect.Type
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

fun `class`(name: String, init: TypeDsl.() -> Unit = {}): TypeSpec {
    return TypeDsl.`class`(name).apply(init).build()
}

fun `class`(name: ClassName, init: TypeDsl.() -> Unit = {}): TypeSpec {
    return TypeDsl.`class`(name).apply(init).build()
}

fun type(name: String, vararg bounds: TypeName): TypeVariableName {
    return TypeVariableName.get(name, *bounds)
}

fun param(type: Type, name: String, init: ParameterDsl.() -> Unit = {}): ParameterSpec {
    return ParameterDsl(type, name).apply(init).build()
}

fun param(type: TypeName, name: String, init: ParameterDsl.() -> Unit = {}): ParameterSpec {
    return ParameterDsl(type, name).apply(init).build()
}

fun param(type: TypeMirror, name: String, init: ParameterDsl.() -> Unit = {}): ParameterSpec {
    return ParameterDsl(TypeName.get(type), name).apply(init).build()
}

fun param(type: KClass<*>, name: String, init: ParameterDsl.() -> Unit = {}): ParameterSpec {
    return ParameterDsl(type.java, name).apply(init).build()
}

fun method(name: String, vararg params: ParameterSpec, init: MethodDsl.() -> Unit): MethodSpec {
    return MethodDsl.method(name).apply {
        params(*params)
    }.apply(init).build()
}

infix fun String.extends(bound: TypeName): TypeVariableName {
    return TypeVariableName.get(this, bound)
}
