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

package io.t28.auto.truth.processor

import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import java.io.File

class FileSubject(
    failureMetadata: FailureMetadata,
    private val actual: File?
) : Subject(failureMetadata, actual) {
    fun exists() {
        if (actual?.exists() == true) {
            return
        }
        failWithActual(Fact.simpleFact("expected to exist"))
    }

    companion object {
        val FACTORY = Factory<FileSubject, File> { metadata, actual ->
            FileSubject(metadata, actual)
        }
    }
}
