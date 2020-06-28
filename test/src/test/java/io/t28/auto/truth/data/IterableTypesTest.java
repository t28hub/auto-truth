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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.truth.IterableSubject;
import com.google.common.truth.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.IterableTypesSubject.assertThat;
import static io.t28.auto.truth.data.IterableTypesSubject.expectFailure;

class IterableTypesTest {
    private IterableTypes underTest;

    @BeforeEach
    void setup() {
        underTest = IterableTypes.builder()
            .iterable(Collections.emptyList())
            .collection(ImmutableList.of("Alice", "Bob", "Charlie"))
            .list(ImmutableList.of(1, 2, 3, 4, 5))
            .set(ImmutableSet.of("foo", "bar"))
            .build();
    }

    @Test
    void shouldReturnIterableSubject() {
        // Act
        final Subject iterableSubject = assertThat(underTest).iterable();
        final Subject collectionSubject = assertThat(underTest).collection();
        final Subject listSubject = assertThat(underTest).list();
        final Subject setSubject = assertThat(underTest).set();

        // Assert
        assertThat(iterableSubject).isInstanceOf(IterableSubject.class);
        assertThat(collectionSubject).isInstanceOf(IterableSubject.class);
        assertThat(listSubject).isInstanceOf(IterableSubject.class);
        assertThat(setSubject).isInstanceOf(IterableSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Act & Assert
        assertThat(underTest).iterable().isEmpty();
        assertThat(underTest).collection().hasSize(3);
        assertThat(underTest).list().containsExactly(1, 2, 3, 4, 5).inOrder();
        assertThat(underTest).set().doesNotContain("baz");
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).collection().containsExactly("Alice", "Bob");
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("iterableTypes.collection()");
        assertThat(error).factValue("expected").isEqualTo("[Alice, Bob]");
        assertThat(error).factValue("but was").isEqualTo("[Alice, Bob, Charlie]");
    }
}
