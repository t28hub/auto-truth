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

import com.google.common.truth.ComparableSubject
import com.google.common.truth.FailureMetadata
import com.google.common.truth.StringSubject
import com.google.common.truth.Subject
import com.google.common.truth.Truth
import com.tschuchort.compiletesting.KotlinCompilation
import io.t28.auto.truth.processor.FileSubject

class ResultSubject(
    failureMetadata: FailureMetadata,
    private val actual: KotlinCompilation.Result
) : Subject(failureMetadata, actual) {
    fun isOk() {
        exitCode().isEqualTo(KotlinCompilation.ExitCode.OK)
    }

    fun isCompilationError() {
        exitCode().isEqualTo(KotlinCompilation.ExitCode.COMPILATION_ERROR)
    }

    fun message(): StringSubject {
        return check("message").that(actual.messages)
    }

    fun generatedFile(name: String): FileSubject {
        return check("generatedFile($name)")
            .about(FileSubject.FACTORY)
            .that(actual.generatedFiles.firstOrNull { file ->
                file.name.endsWith(name)
            })
    }

    private fun exitCode(): ComparableSubject<KotlinCompilation.ExitCode> {
        return check("exitCode").about<ComparableSubject<KotlinCompilation.ExitCode>, KotlinCompilation.ExitCode> { metadata, _actual ->
            return@about object : ComparableSubject<KotlinCompilation.ExitCode>(metadata, _actual) {}
        }.that(actual.exitCode)
    }

    companion object {
        val FACTORY = Factory<ResultSubject, KotlinCompilation.Result> { metadata, actual ->
            ResultSubject(metadata, actual)
        }

        fun assertThat(actual: KotlinCompilation.Result): ResultSubject {
            return Truth.assertAbout(FACTORY).that(actual)
        }
    }
}
