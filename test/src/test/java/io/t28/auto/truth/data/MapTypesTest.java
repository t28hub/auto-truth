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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.MapSubject;
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
import static io.t28.auto.truth.data.MapTypesTest.MapTypesSubject.assertThat;
import static io.t28.auto.truth.data.MapTypesTest.MapTypesSubject.expectFailure;

class MapTypesTest {
    private MapTypes underTest;

    @BeforeEach
    void setup() {
        underTest = MapTypes.builder()
            .map(ImmutableMap.of("type", "User"))
            .sortedMap(ImmutableSortedMap.of(1L, "Alice", 2L, "Bob"))
            .build();
    }

    @Test
    void shouldReturnMapSubject() {
        // Act
        final Subject mapSubject = assertThat(underTest).map();
        final Subject sortedMapSubject = assertThat(underTest).sortedMap();

        // Assert
        assertThat(mapSubject).isInstanceOf(MapSubject.class);
        assertThat(sortedMapSubject).isInstanceOf(MapSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).map().hasSize(1);
        assertThat(underTest).sortedMap().containsKey(1L);
        assertThat(underTest).sortedMap().containsEntry(2L, "Bob");
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).sortedMap().containsEntry(1L, "Charlie");
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("mapTypes.sortedMap().get(1)");
        assertThat(error).factValue("expected").isEqualTo("Charlie");
        assertThat(error).factValue("but was").isEqualTo("Alice");
    }

    @AutoSubject(MapTypes.class)
    public static class MapTypesSubject extends AutoMapTypesSubject {
        protected MapTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable MapTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static MapTypesSubject assertThat(@Nullable MapTypes actual) {
            return assertAbout(MapTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<MapTypesSubject, MapTypes> callback) {
            return expectFailureAbout(MapTypesSubject::new, callback);
        }
    }
}
