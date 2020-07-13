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

package io.t28.auto.truth.data;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.Test;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.NoTypesTest.NoTypesSubject.assertThat;

class NoTypesTest {
    @Test
    void shouldGenerateNoAssertionMethod() {
        // Act
        final Subject subject = assertThat(new NoTypes());

        // Assert
        assertThat(subject).isNotNull();
    }

    @AutoSubject(NoTypes.class)
    public static class NoTypesSubject extends AutoNoTypesSubject {
        protected NoTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable NoTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static NoTypesSubject assertThat(@Nullable NoTypes actual) {
            return assertAbout(NoTypesSubject::new).that(actual);
        }
    }
}
