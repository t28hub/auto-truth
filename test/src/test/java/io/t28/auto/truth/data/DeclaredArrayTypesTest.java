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
import com.google.common.truth.ObjectArraySubject;
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
import static io.t28.auto.truth.data.DeclaredArrayTypesTest.DeclaredArrayTypesSubject.assertThat;
import static io.t28.auto.truth.data.DeclaredArrayTypesTest.DeclaredArrayTypesSubject.expectFailure;

class DeclaredArrayTypesTest {
    private DeclaredArrayTypes underTest;

    @BeforeEach
    void setup() {
        underTest = DeclaredArrayTypes.builder()
            .objects(Short.class, Integer.class, Long.class)
            .strings("foo", "bar")
            .build();
    }

    @Test
    void shouldReturnObjectArraySubject() {
        // Act
        final Subject objectArraySubject = assertThat(underTest).objectArray();
        final Subject stringArraySubject = assertThat(underTest).stringArray();

        // Assert
        assertThat(objectArraySubject).isInstanceOf(ObjectArraySubject.class);
        assertThat(stringArraySubject).isInstanceOf(ObjectArraySubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).stringArray().hasLength(2);
        assertThat(underTest).stringArray().asList().containsExactly("foo", "bar");
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).objectArray().asList().containsExactly(Short.class, Integer.class);
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("declaredArrayTypes.objectArray().asList()");
        assertThat(error).factValue("expected", 0).isEqualTo("[class java.lang.Short, class java.lang.Integer]");
        assertThat(error).factValue("but was", 0).isEqualTo("[class java.lang.Short, class java.lang.Integer, class java.lang.Long]");
    }

    @AutoSubject(DeclaredArrayTypes.class)
    public static class DeclaredArrayTypesSubject extends AutoDeclaredArrayTypesSubject {
        protected DeclaredArrayTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable DeclaredArrayTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static DeclaredArrayTypesSubject assertThat(@Nullable DeclaredArrayTypes actual) {
            return assertAbout(DeclaredArrayTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<DeclaredArrayTypesSubject, DeclaredArrayTypes> callback) {
            return expectFailureAbout(DeclaredArrayTypesSubject::new, callback);
        }
    }
}
