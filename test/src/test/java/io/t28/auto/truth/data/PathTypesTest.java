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
import com.google.common.truth.PathSubject;
import com.google.common.truth.Subject;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.PathTypesTest.PathTypesSubject.assertThat;
import static io.t28.auto.truth.data.PathTypesTest.PathTypesSubject.expectFailure;

class PathTypesTest {
    @TempDir
    static Path tempDir;

    private PathTypes underTest;

    @BeforeEach
    void setup() {
        underTest = PathTypes.builder()
            .path(tempDir)
            .build();
    }

    @Test
    void shouldReturnPathSubject() {
        // Act
        final Subject pathSubject = assertThat(underTest).path();

        // Assert
        assertThat(pathSubject).isInstanceOf(PathSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).path().isNotNull();
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).path().isNull();
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("pathTypes.path()");
        assertThat(error).factValue("expected").isEqualTo("null");
        assertThat(error).factValue("but was").isNotEmpty();
    }

    @AutoSubject(PathTypes.class)
    public static class PathTypesSubject extends AutoPathTypesSubject {
        protected PathTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable PathTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static PathTypesSubject assertThat(@Nullable PathTypes actual) {
            return assertAbout(PathTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<PathTypesSubject, PathTypes> callback) {
            return expectFailureAbout(PathTypesSubject::new, callback);
        }
    }
}
