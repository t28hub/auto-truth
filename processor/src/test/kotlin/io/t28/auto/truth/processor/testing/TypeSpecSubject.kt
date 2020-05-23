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

package io.t28.auto.truth.processor.testing

import com.google.common.truth.FailureMetadata
import com.google.common.truth.IterableSubject
import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertAbout
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.t28.auto.truth.processor.testing.FieldSpecSubject.Companion.fieldSpec
import io.t28.auto.truth.processor.testing.MethodSpecSubject.Companion.methodSpec
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate")
class TypeSpecSubject private constructor(failureMetadata: FailureMetadata, private val actual: TypeSpec?) : Subject(failureMetadata, actual) {
    fun hasName(expected: String) {
        check("name").that(actual?.name).isEqualTo(expected)
    }

    fun modifiers(): IterableSubject {
        return check("modifiers").that(actual?.modifiers)
    }

    fun hasSuperclass(expected: KClass<*>) {
        hasSuperclass(TypeName.get(expected.java))
    }

    fun hasSuperclass(expected: TypeName) {
        check("superclass").that(actual?.superclass).isEqualTo(expected)
    }

    fun fieldSpec(filter: (FieldSpec) -> Boolean): FieldSpecSubject {
        return check("fieldSpec").about(fieldSpec()).that(actual?.fieldSpecs?.first(filter))
    }

    fun methodSpec(filter: (MethodSpec) -> Boolean): MethodSpecSubject {
        return check("methodSpec").about(methodSpec()).that(actual?.methodSpecs?.first(filter))
    }

    companion object {
        fun assertThat(spec: TypeSpec?): TypeSpecSubject {
            return assertAbout(typeSpec()).that(spec)
        }

        fun typeSpec(): Factory<TypeSpecSubject, TypeSpec> {
            return Factory<TypeSpecSubject, TypeSpec> { metadata, spec -> TypeSpecSubject(metadata, spec) }
        }
    }
}
