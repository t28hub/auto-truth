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

package io.t28.auto.truth.processor.generator.method

import com.google.common.truth.ObjectArraySubject
import com.google.common.truth.PrimitiveBooleanArraySubject
import com.google.common.truth.PrimitiveByteArraySubject
import com.google.common.truth.PrimitiveCharArraySubject
import com.google.common.truth.PrimitiveDoubleArraySubject
import com.google.common.truth.PrimitiveFloatArraySubject
import com.google.common.truth.PrimitiveIntArraySubject
import com.google.common.truth.PrimitiveLongArraySubject
import com.google.common.truth.PrimitiveShortArraySubject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import javax.lang.model.type.ArrayType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeKind.BYTE
import javax.lang.model.type.TypeKind.CHAR
import javax.lang.model.type.TypeKind.DOUBLE
import javax.lang.model.type.TypeKind.FLOAT
import javax.lang.model.type.TypeKind.INT
import javax.lang.model.type.TypeKind.LONG
import javax.lang.model.type.TypeKind.SHORT
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleTypeVisitor8

class AbstractArraySubjectGenerator(context: Context) : TruthSubjectGenerator(context) {
    override fun matches(type: ArrayType): Boolean {
        return true
    }

    override fun subjectClass(type: TypeMirror): TypeName {
        return (type as? ArrayType)
            ?.componentType
            ?.accept(SubjectTypeResolver, Unit)
            ?: throw IllegalArgumentException("Supported type is only ArrayType, but not: $type")
    }

    internal object SubjectTypeResolver : SimpleTypeVisitor8<TypeName, Unit>() {
        override fun visitPrimitive(type: PrimitiveType, parameter: Unit): TypeName {
            return when (type.kind) {
                BOOLEAN -> ClassName.get(PrimitiveBooleanArraySubject::class.java)
                BYTE -> ClassName.get(PrimitiveByteArraySubject::class.java)
                SHORT -> ClassName.get(PrimitiveShortArraySubject::class.java)
                INT -> ClassName.get(PrimitiveIntArraySubject::class.java)
                LONG -> ClassName.get(PrimitiveLongArraySubject::class.java)
                CHAR -> ClassName.get(PrimitiveCharArraySubject::class.java)
                FLOAT -> ClassName.get(PrimitiveFloatArraySubject::class.java)
                DOUBLE -> ClassName.get(PrimitiveDoubleArraySubject::class.java)
                else -> throw IllegalArgumentException("Unknown primitive type: ${type.kind}")
            }
        }

        override fun defaultAction(type: TypeMirror, parameter: Unit): TypeName {
            return ParameterizedTypeName.get(ClassName.get(ObjectArraySubject::class.java), TypeName.get(type))
        }
    }
}
