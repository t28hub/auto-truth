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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableTable;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.GuavaOptionalSubject;
import com.google.common.truth.MultimapSubject;
import com.google.common.truth.MultisetSubject;
import com.google.common.truth.Subject;
import com.google.common.truth.TableSubject;
import io.t28.auto.truth.AutoSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.GuavaTypesTest.GuavaTypesSubject.assertThat;
import static io.t28.auto.truth.data.GuavaTypesTest.GuavaTypesSubject.expectFailure;

class GuavaTypesTest {
    private GuavaTypes underTest;

    @BeforeEach
    void setup() {
        underTest = GuavaTypes.builder()
            .optional("Foo")
            .multiset(ImmutableMultiset.of("Alice", "Bob", "Bob"))
            .multimap(ImmutableMultimap.of("name", "Alice"))
            .table(ImmutableTable.of("Row", "Column", 100))
            .build();
    }

    @Nested
    class Optional {
        @Test
        void shouldReturnGuavaOptionalSubject() {
            // Act
            final Subject subject = assertThat(underTest).optional();

            // Assert
            assertThat(subject).isInstanceOf(GuavaOptionalSubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).optional().isPresent();
            assertThat(underTest).optional().hasValue("Foo");
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).optional().hasValue("Bar");
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("guavaTypes.optional().get()");
            assertThat(error).factValue("expected").isEqualTo("Bar");
            assertThat(error).factValue("but was").isEqualTo("Foo");
        }
    }

    @Nested
    class Multiset {
        @Test
        void shouldReturnMultisetSubject() {
            // Act
            final Subject subject = assertThat(underTest).multiset();

            // Assert
            assertThat(subject).isInstanceOf(MultisetSubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).multiset().hasCount("Alice", 1);
            assertThat(underTest).multiset().hasCount("Bob", 2);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).multiset().hasCount("Charlie", 1);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("guavaTypes.multiset().count(Charlie)");
            assertThat(error).factValue("expected").isEqualTo("1");
            assertThat(error).factValue("but was").isEqualTo("0");
        }
    }

    @Nested
    class Multimap {
        @Test
        void shouldReturnMultimapSubject() {
            // Act
            final Subject subject = assertThat(underTest).multimap();

            // Assert
            assertThat(subject).isInstanceOf(MultimapSubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).multimap().isNotEmpty();
            assertThat(underTest).multimap().containsExactly("name", "Alice");
            assertThat(underTest).multimap().doesNotContainEntry("name", "Bob");
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).multimap().containsAtLeast("name", "Charlie");
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("guavaTypes.multimap()");
            assertThat(error).factKeys().contains("Not true that <{name=[Alice]}> contains at least <{name=[Charlie]}>. It is missing <{name=[Charlie]}>");
        }
    }

    @Nested
    class Table {
        @Test
        void shouldReturnTableSubject() {
            // Act
            final Subject subject = assertThat(underTest).table();

            // Assert
            assertThat(subject).isInstanceOf(TableSubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).table().isNotEmpty();
            assertThat(underTest).table().containsCell("Row", "Column", 100);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).table().isEmpty();
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("guavaTypes.table()");
            assertThat(error).factKeys().contains("expected to be empty");
            assertThat(error).factValue("but was").isEqualTo("{Row={Column=100}}");
        }
    }

    @AutoSubject(GuavaTypes.class)
    public static class GuavaTypesSubject extends AutoGuavaTypesSubject {
        protected GuavaTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable GuavaTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static GuavaTypesSubject assertThat(@Nullable GuavaTypes actual) {
            return assertAbout(GuavaTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<GuavaTypesSubject, GuavaTypes> callback) {
            return expectFailureAbout(GuavaTypesSubject::new, callback);
        }
    }
}
