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

package io.t28.auto.truth.processor.translator.property

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.translator.PropertyTranslator
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ErrorType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleTypeVisitor8

class DefaultPropertyTranslator : PropertyTranslator {
    companion object {
        private val BOXED_VOID = ClassName.get("java.lang", "Void")
        private val BOXED_BOOLEAN = ClassName.get("java.lang", "Boolean")
    }

    override fun matches(type: TypeMirror): Boolean {
        return type.accept(object : SimpleTypeVisitor8<Boolean, Unit>() {
            override fun visitPrimitive(type: PrimitiveType, parameter: Unit) = type.kind != BOOLEAN

            override fun visitDeclared(type: DeclaredType, parameter: Unit): Boolean {
                val typeName = TypeName.get(type)
                return (typeName != BOXED_VOID) and (typeName != BOXED_BOOLEAN)
            }

            override fun visitError(type: ErrorType, parameter: Unit): Boolean {
                return visitDeclared(type, parameter)
            }

            override fun defaultAction(type: TypeMirror, parameter: Unit) = false
        }, Unit)
    }

    override fun translate(input: Property): MethodSpec {
        require(matches(input.type))

        return MethodSpec.methodBuilder("has${input.name.capitalize()}").apply {
            addModifiers(PUBLIC)
            addParameter(ParameterSpec.builder(TypeName.get(input.type), "expected").build())
            addStatement("check(\$S).that(this.\$L.\$L).isEqualTo(\$L)", input.symbol, "actual", input.symbol, "expected")
        }.build()
    }
}
