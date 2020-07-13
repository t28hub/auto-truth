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
import com.google.common.truth.OptionalDoubleSubject;
import com.google.common.truth.OptionalIntSubject;
import com.google.common.truth.OptionalLongSubject;
import com.google.common.truth.OptionalSubject;
import com.google.common.truth.Subject;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.OptionalTypesTest.OptionalTypesSubject.assertThat;
import static io.t28.auto.truth.data.OptionalTypesTest.OptionalTypesSubject.expectFailure;

class OptionalTypesTest {
    private OptionalTypes underTest;

    @BeforeEach
    void setup() {
        underTest = OptionalTypes.builder()
            .optionalInt(23)
            .optionalDouble(Double.NEGATIVE_INFINITY)
            .optionalString("Alice")
            .build();
    }

    @Test
    void shouldReturnOptionalSubject() {
        // Act
        final Subject optionalIntSubject = assertThat(underTest).optionalInt();
        final Subject optionalLongSubject = assertThat(underTest).optionalLong();
        final Subject optionalDoubleSubject = assertThat(underTest).optionalDouble();
        final Subject optionalSubject = assertThat(underTest).optionalString();

        // Assert
        assertThat(optionalIntSubject).isInstanceOf(OptionalIntSubject.class);
        assertThat(optionalLongSubject).isInstanceOf(OptionalLongSubject.class);
        assertThat(optionalDoubleSubject).isInstanceOf(OptionalDoubleSubject.class);
        assertThat(optionalSubject).isInstanceOf(OptionalSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).optionalInt().hasValue(23);
        assertThat(underTest).optionalLong().isEmpty();
        assertThat(underTest).optionalDouble().isPresent();
        assertThat(underTest).optionalString().hasValue("Alice");
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).optionalString().hasValue("Bob");
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("optionalTypes.optionalString().get()");
        assertThat(error).factValue("expected").isEqualTo("Bob");
        assertThat(error).factValue("but was").isEqualTo("Alice");
    }

    @AutoSubject(OptionalTypes.class)
    public static class OptionalTypesSubject extends AutoOptionalTypesSubject {
        protected OptionalTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable OptionalTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static OptionalTypesSubject assertThat(@Nullable OptionalTypes actual) {
            return assertAbout(OptionalTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<OptionalTypesSubject, OptionalTypes> callback) {
            return expectFailureAbout(OptionalTypesSubject::new, callback);
        }
    }
}
