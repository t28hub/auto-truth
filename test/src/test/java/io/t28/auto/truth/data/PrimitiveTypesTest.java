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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;
import static io.t28.auto.truth.data.PrimitiveTypesTest.PrimitiveTypesSubject.assertThat;
import static io.t28.auto.truth.data.PrimitiveTypesTest.PrimitiveTypesSubject.expectFailure;
import static org.junit.jupiter.api.Assertions.assertAll;

class PrimitiveTypesTest {
    private PrimitiveTypes underTest;

    @BeforeEach
    void setup() {
        underTest = PrimitiveTypes.builder()
            .booleanValue(true)
            .byteValue((byte) 1)
            .charValue('a')
            .shortValue((short) 2)
            .intValue(43)
            .longValue(1024L)
            .floatValue(12.3f)
            .doubleValue(1.234567890)
            .build();
    }

    @Nested
    class Boolean {
        @Test
        void shouldPassAssertion() {
            assertAll(
                () -> {
                    assertThat(underTest).isBooleanValue();
                },
                () -> {
                    assertThat(underTest.toBuilder()
                        .booleanValue(false)
                        .build()).isNotBooleanValue();
                }
            );
        }

        @Test
        void shouldNotPassAssertion() {
            assertAll(
                () -> {
                    final AssertionError error = expectFailure(callback -> {
                        callback.that(underTest).isNotBooleanValue();
                    });

                    assertThat(error)
                        .factKeys()
                        .contains("expected not to be booleanValue");
                },
                () -> {
                    final AssertionError error = expectFailure(callback -> {
                        callback.that(underTest.toBuilder()
                            .booleanValue(false)
                            .build()).isBooleanValue();
                    });

                    assertThat(error)
                        .factKeys()
                        .contains("expected to be booleanValue");
                }
            );
        }
    }

    @Nested
    class Byte {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasByteValue((byte) 1);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasByteValue((byte) 2);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.byteValue()");
            assertThat(error).factValue("expected").isEqualTo("2");
            assertThat(error).factValue("but was").isEqualTo("1");
        }
    }

    @Nested
    class Char {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasCharValue('a');
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasCharValue('b');
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.charValue()");
            assertThat(error).factValue("expected").isEqualTo("b");
            assertThat(error).factValue("but was").isEqualTo("a");
        }
    }

    @Nested
    class Short {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasShortValue((short) 2);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasShortValue((short) 3);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.shortValue()");
            assertThat(error).factValue("expected").isEqualTo("3");
            assertThat(error).factValue("but was").isEqualTo("2");
        }
    }

    @Nested
    class Int {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasIntValue(43);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasIntValue(34);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.intValue()");
            assertThat(error).factValue("expected").isEqualTo("34");
            assertThat(error).factValue("but was").isEqualTo("43");
        }
    }

    @Nested
    class Long {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasLongValue(1024L);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasLongValue(4096L);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.longValue()");
            assertThat(error).factValue("expected").isEqualTo("4096");
            assertThat(error).factValue("but was").isEqualTo("1024");
        }
    }

    @Nested
    class Float {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasFloatValue(12.3f);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasFloatValue(12.34f);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.floatValue()");
            assertThat(error).factValue("expected").isEqualTo("12.34");
            assertThat(error).factValue("but was").isEqualTo("12.3");
        }
    }

    @Nested
    class Double {
        @Test
        void shouldPassAssertion() {
            assertThat(underTest).hasDoubleValue(1.234567890);
        }

        @Test
        void shouldNotPassAssertion() {
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasDoubleValue(1.2345678);
            });

            assertThat(error).factValue("value of").isEqualTo("primitiveTypes.doubleValue()");
            assertThat(error).factValue("expected").isEqualTo("1.2345678");
            assertThat(error).factValue("but was").isEqualTo("1.23456789");
        }
    }

    @AutoSubject(PrimitiveTypes.class)
    public static class PrimitiveTypesSubject extends AutoPrimitiveTypesSubject {
        protected PrimitiveTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable PrimitiveTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static PrimitiveTypesSubject assertThat(@Nullable PrimitiveTypes actual) {
            return assertAbout(PrimitiveTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<PrimitiveTypesSubject, PrimitiveTypes> callback) {
            return expectFailureAbout(PrimitiveTypesSubject::new, callback);
        }
    }
}
