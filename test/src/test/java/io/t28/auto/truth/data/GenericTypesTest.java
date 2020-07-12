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

import com.google.common.truth.ExpectFailure;
import com.google.common.truth.FailureMetadata;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.Test;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static io.t28.auto.truth.data.GenericTypesTest.GenericTypesSubject.assertThat;
import static io.t28.auto.truth.data.GenericTypesTest.GenericTypesSubject.expectFailure;

class GenericTypesTest {
    @Test
    void shouldPassAssertion() {
        // Act & Assert
        final GenericTypes<String> underTest = new GenericTypes<>("foo_bar_baz");
        assertThat(underTest).isNotNull();
        assertThat(underTest).hasValue("foo_bar_baz");
    }

    @Test
    void shouldFailAssertion() {
        // Act & Assert
        final AssertionError error = expectFailure((ExpectFailure.SimpleSubjectBuilderCallback<GenericTypesSubject<String>, GenericTypes<String>>) callback -> {
            final GenericTypes<String> underTest = new GenericTypes<>("foo_bar_baz");
            callback.that(underTest).hasValue("foo_bar");
        });
        assertThat(error).factValue("value of").isEqualTo("genericTypes.value()");
        assertThat(error).factValue("expected").isEqualTo("foo_bar");
        assertThat(error).factValue("but was").isEqualTo("foo_bar_baz");
    }

    @AutoSubject(GenericTypes.class)
    static class GenericTypesSubject<T extends CharSequence> extends AutoGenericTypesSubject<T> {
        GenericTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable GenericTypes<T> actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static <T extends CharSequence> GenericTypesSubject<T> assertThat(GenericTypes<T> actual) {
            return assertAbout((Factory<GenericTypesSubject<T>, GenericTypes<T>>) GenericTypesSubject::new).that(actual);
        }

        @Nonnull
        public static <T extends CharSequence> AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<GenericTypesSubject<T>, GenericTypes<T>> callback) {
            return expectFailureAbout(GenericTypesSubject::new, callback);
        }
    }
}
