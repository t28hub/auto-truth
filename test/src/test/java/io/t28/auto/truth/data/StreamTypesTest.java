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
import com.google.common.truth.IntStreamSubject;
import com.google.common.truth.LongStreamSubject;
import com.google.common.truth.StreamSubject;
import com.google.common.truth.Subject;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.StreamTypesTest.StreamTypesSubject.assertThat;
import static io.t28.auto.truth.data.StreamTypesTest.StreamTypesSubject.expectFailure;

class StreamTypesTest {
    private StreamTypes underTest;

    @BeforeEach
    void setup() {
        underTest = StreamTypes.builder()
            .intStream(IntStream.of(1))
            .longStream(LongStream.empty())
            .doubleStream(DoubleStream.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY))
            .stringStream(Stream.of("Alice", "Bob", "Charlie"))
            .build();
    }

    @Test
    void shouldReturnStreamSubject() {
        // Act
        final Subject intStreamSubject = assertThat(underTest).intStream();
        final Subject longStreamSubject = assertThat(underTest).longStream();
        final Subject stringStreamSubject = assertThat(underTest).stringStream();

        // Assert
        assertThat(intStreamSubject).isInstanceOf(IntStreamSubject.class);
        assertThat(longStreamSubject).isInstanceOf(LongStreamSubject.class);
        assertThat(stringStreamSubject).isInstanceOf(StreamSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).intStream().hasSize(1);
        assertThat(underTest).longStream().isEmpty();
        assertThat(underTest).stringStream().containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void shouldFailAssertion() {
        // Assert
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).stringStream().contains("Dave");
        });

        // Act
        assertThat(error).factValue("expected to contain").isEqualTo("Dave");
        assertThat(error).factValue("but was").isEqualTo("[Alice, Bob, Charlie]");
    }

    @AutoSubject(StreamTypes.class)
    public static class StreamTypesSubject extends AutoStreamTypesSubject {
        protected StreamTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable StreamTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static StreamTypesSubject assertThat(@Nullable StreamTypes actual) {
            return assertAbout(StreamTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<StreamTypesSubject, StreamTypes> callback) {
            return expectFailureAbout(StreamTypesSubject::new, callback);
        }
    }
}
