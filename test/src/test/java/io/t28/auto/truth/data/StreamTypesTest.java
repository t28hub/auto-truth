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

import com.google.common.truth.IntStreamSubject;
import com.google.common.truth.LongStreamSubject;
import com.google.common.truth.StreamSubject;
import com.google.common.truth.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.StreamTypesSubject.assertThat;
import static io.t28.auto.truth.data.StreamTypesSubject.expectFailure;

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
}
