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

package io.t28.auto.truth.processor.extensions

import com.google.common.truth.Truth.assertThat
import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.testing.Resource
import io.t28.auto.truth.processor.testing.process
import org.junit.jupiter.api.Test

internal class RoundEnvironmentTest {
    @Test
    fun `getAnnotatedElements should return set of annotated element`() {
        process(Resource.User, Resource.UserSubject) { context ->
            // Act
            val elements = context.roundEnv.getAnnotatedElements<AutoSubject>()

            // Assert
            assertThat(elements).hasSize(1)
        }.compilesWithoutError()
    }

    @Test
    fun `getAnnotatedElements should return empty set when no annotated element`() {
        process(Resource.User, Resource.UserSubject) { context ->
            // Act
            val elements = context.roundEnv.getAnnotatedElements<Deprecated>()

            // Assert
            assertThat(elements).isEmpty()
        }.compilesWithoutError()
    }
}
