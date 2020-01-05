/*
 * Copyright 2020 Tatsuya Maki
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

import com.google.common.truth.OptionalDoubleSubject
import com.google.common.truth.OptionalIntSubject
import com.google.common.truth.OptionalLongSubject
import com.google.common.truth.OptionalSubject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.utils.getDeclaredType
import java.lang.IllegalArgumentException
import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class OptionalSubjectGenerator(context: Context) : Truth8SubjectGenerator(context) {
    override fun matches(type: TypeMirror): Boolean {
        return type.accept(GuavaOptionalTypeMatcher, context)
    }

    override fun factoryMethodName(type: TypeMirror): String {
        return type.accept(object : OptionalTypeVisitor<String> {
            override fun visitOptional(type: TypeMirror) = "optionals"

            override fun visitOptionalInt(type: TypeMirror) = "optionalInts"

            override fun visitOptionalLong(type: TypeMirror) = "optionalLongs"

            override fun visitOptionalDouble(type: TypeMirror) = "optionalDoubles"
        })
    }

    override fun subjectClass(type: TypeMirror): TypeName {
        return type.accept(object : OptionalTypeVisitor<TypeName> {
            override fun visitOptional(type: TypeMirror) = ClassName.get(OptionalSubject::class.java)

            override fun visitOptionalInt(type: TypeMirror) = ClassName.get(OptionalIntSubject::class.java)

            override fun visitOptionalLong(type: TypeMirror) = ClassName.get(OptionalLongSubject::class.java)

            override fun visitOptionalDouble(type: TypeMirror) = ClassName.get(OptionalDoubleSubject::class.java)
        })
    }

    internal fun <R : Any> TypeMirror.accept(visitor: OptionalTypeVisitor<R>): R {
        val utils = context.utils
        return when {
            utils.isAssignableType(this, utils.getDeclaredType<Optional<*>>()) -> {
                visitor.visitOptional(this)
            }
            utils.isAssignableType(this, utils.getDeclaredType<OptionalInt>()) -> {
                visitor.visitOptionalInt(this)
            }
            utils.isAssignableType(this, utils.getDeclaredType<OptionalLong>()) -> {
                visitor.visitOptionalLong(this)
            }
            utils.isAssignableType(this, utils.getDeclaredType<OptionalDouble>()) -> {
                visitor.visitOptionalDouble(this)
            }
            else -> {
                throw IllegalArgumentException("Unsupported type and not Optional: $this")
            }
        }
    }

    internal interface OptionalTypeVisitor<out R : Any> {
        fun visitOptional(type: TypeMirror): R

        fun visitOptionalInt(type: TypeMirror): R

        fun visitOptionalLong(type: TypeMirror): R

        fun visitOptionalDouble(type: TypeMirror): R
    }

    internal object GuavaOptionalTypeMatcher : SupportedTypeMatcher() {
        private val supportedClasses = arrayOf(
            Optional::class,
            OptionalInt::class,
            OptionalLong::class,
            OptionalDouble::class
        )

        override fun visitDeclared(type: DeclaredType, context: Context): Boolean {
            val utils = context.utils
            return supportedClasses
                .map { utils.getDeclaredType(it) }
                .any { utils.isAssignableType(type, it) }
        }
    }
}
