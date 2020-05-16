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
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName

class MethodSpecSubject private constructor(failureMetadata: FailureMetadata, private val actual: MethodSpec?) : Subject(failureMetadata, actual) {
    fun hasName(expected: String) {
        check("name").that(actual?.name).isEqualTo(expected)
    }

    fun modifiers(): IterableSubject {
        return check("modifiers").that(actual?.modifiers)
    }

    fun hasReturnType(expected: TypeName) {
        check("returnType").that(actual?.returnType).isEqualTo(expected)
    }

    fun parameters(): IterableSubject {
        return check("parameters").that(actual?.parameters)
    }

    companion object {
        fun assertThat(spec: MethodSpec?): MethodSpecSubject {
            return assertAbout(methodSpec()).that(spec)
        }

        private fun methodSpec(): Factory<MethodSpecSubject, MethodSpec> {
            return Factory<MethodSpecSubject, MethodSpec> { metadata, spec -> MethodSpecSubject(metadata, spec) }
        }
    }
}
