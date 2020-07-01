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

import com.google.common.truth.IntStreamSubject
import com.google.common.truth.LongStreamSubject
import com.google.common.truth.StreamSubject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.utils.TypeUtils
import io.t28.auto.truth.processor.utils.isAssignable
import java.lang.IllegalArgumentException
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class StreamSubjectGenerator(
    context: Context,
    private val utils: TypeUtils = context.utils
) : Truth8SubjectGenerator(context) {
    override fun matches(type: DeclaredType): Boolean {
        return arrayOf(Stream::class, IntStream::class, LongStream::class)
            .mapNotNull { supportedClass -> utils.getDeclaredType(supportedClass) }
            .any { supportedType -> utils.isAssignableType(type, supportedType) }
    }

    override fun factoryMethodName(type: TypeMirror): String {
        return type.accept(object : StreamTypeVisitor<String> {
            override fun visitStream(type: TypeMirror) = "streams"

            override fun visitIntStream(type: TypeMirror) = "intStreams"

            override fun visitLongStream(type: TypeMirror) = "longStreams"
        })
    }

    override fun subjectClass(type: TypeMirror): TypeName {
        return type.accept(object : StreamTypeVisitor<TypeName> {
            override fun visitStream(type: TypeMirror) = ClassName.get(StreamSubject::class.java)

            override fun visitIntStream(type: TypeMirror) = ClassName.get(IntStreamSubject::class.java)

            override fun visitLongStream(type: TypeMirror) = ClassName.get(LongStreamSubject::class.java)
        })
    }

    private fun <R : Any> TypeMirror.accept(visitor: StreamTypeVisitor<R>): R {
        return when {
            utils.isAssignable<Stream<*>>(this) -> visitor.visitStream(this)
            utils.isAssignable<IntStream>(this) -> visitor.visitIntStream(this)
            utils.isAssignable<LongStream>(this) -> visitor.visitLongStream(this)
            else -> throw IllegalArgumentException("Unsupported and non-Stream type: $this")
        }
    }

    internal interface StreamTypeVisitor<out R : Any> {
        fun visitStream(type: TypeMirror): R

        fun visitIntStream(type: TypeMirror): R

        fun visitLongStream(type: TypeMirror): R
    }
}
