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

import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback;
import com.google.common.truth.Subject.Factory;
import io.t28.auto.truth.AutoSubject;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static io.t28.auto.truth.data.AutoBoxedPrimitiveTypesSubject.assertThat;

class BoxedPrimitiveTypesTest {
    private BoxedPrimitiveTypes underTest;

    @BeforeEach
    void setup() {
        underTest = BoxedPrimitiveTypes.builder()
            .build();
    }

    @Nested
    class BooleanTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasBooleanValue(null);
            assertThat(underTest.toBuilder().booleanValue(true).build()).isBooleanValue();
            assertThat(underTest.toBuilder().booleanValue(false).build()).isNotBooleanValue();
        }

        @Test
        void shouldFailPositiveAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .booleanValue(false)
                    .build();
                callback.that(actual).isBooleanValue();
            });

            // Assert
            assertThat(error).factKeys().contains("expected to be booleanValue");
            assertThat(error).factValue("but was").contains("booleanValue=false");
        }

        @Test
        void shouldFailNegativeAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .booleanValue(true)
                    .build();
                callback.that(actual).isNotBooleanValue();
            });

            // Assert
            assertThat(error).factKeys().contains("expected not to be booleanValue");
            assertThat(error).factValue("but was").contains("booleanValue=true");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).isBooleanValue();
            });

            // Assert
            assertThat(error).factKeys().contains("expected to be booleanValue");
            assertThat(error).factValue("but was").contains("booleanValue=null");
        }
    }

    @Nested
    class ByteTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasByteValue(null);
            assertThat(underTest.toBuilder().byteValue(Byte.valueOf("1")).build()).hasByteValue((byte) 1);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .byteValue((byte) 1)
                    .build();
                callback.that(actual).hasByteValue((byte) 2);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.byteValue()");
            assertThat(error).factValue("expected").isEqualTo("2");
            assertThat(error).factValue("but was").isEqualTo("1");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasByteValue(Byte.valueOf("1"));
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.byteValue()");
            assertThat(error).factValue("expected").isEqualTo("1");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class CharacterTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasCharacterValue(null);
            assertThat(underTest.toBuilder().characterValue('a').build()).hasCharacterValue('a');
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .characterValue('a')
                    .build();
                callback.that(actual).hasCharacterValue('b');
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.characterValue()");
            assertThat(error).factValue("expected").isEqualTo("b");
            assertThat(error).factValue("but was").isEqualTo("a");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasCharacterValue('a');
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.characterValue()");
            assertThat(error).factValue("expected").isEqualTo("a");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class ShortTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasShortValue(null);
            assertThat(underTest.toBuilder().shortValue((short) 1).build()).hasShortValue((short) 1);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .shortValue((short) 1)
                    .build();
                callback.that(actual).hasShortValue((short) 2);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.shortValue()");
            assertThat(error).factValue("expected").isEqualTo("2");
            assertThat(error).factValue("but was").isEqualTo("1");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasShortValue((short) 1);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.shortValue()");
            assertThat(error).factValue("expected").isEqualTo("1");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class IntegerTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasIntegerValue(null);
            assertThat(underTest.toBuilder().integerValue(43).build()).hasIntegerValue(43);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .integerValue(43)
                    .build();
                callback.that(actual).hasIntegerValue(34);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.integerValue()");
            assertThat(error).factValue("expected").isEqualTo("34");
            assertThat(error).factValue("but was").isEqualTo("43");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasIntegerValue(43);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.integerValue()");
            assertThat(error).factValue("expected").isEqualTo("43");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class LongTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasLongValue(null);
            assertThat(underTest.toBuilder().longValue(1024L).build()).hasLongValue(1024L);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .longValue(1024L)
                    .build();
                callback.that(actual).hasLongValue(1000L);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.longValue()");
            assertThat(error).factValue("expected").isEqualTo("1000");
            assertThat(error).factValue("but was").isEqualTo("1024");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasLongValue(1024L);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.longValue()");
            assertThat(error).factValue("expected").isEqualTo("1024");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class FloatTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasFloatValue(null);
            assertThat(underTest.toBuilder().floatValue(1.024f).build()).hasFloatValue(1.024f);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .floatValue(1.024f)
                    .build();
                callback.that(actual).hasFloatValue(1.02401f);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.floatValue()");
            assertThat(error).factValue("expected").isEqualTo("1.02401");
            assertThat(error).factValue("but was").isEqualTo("1.024");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasFloatValue(1.024f);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.floatValue()");
            assertThat(error).factValue("expected").isEqualTo("1.024");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }

    @Nested
    class DoubleTest {
        @Test
        void shouldPassAssertion() {
            // Act & Assert
            assertThat(underTest).hasDoubleValue(null);
            assertThat(underTest.toBuilder().doubleValue(Double.POSITIVE_INFINITY).build()).hasDoubleValue(Double.POSITIVE_INFINITY);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                final BoxedPrimitiveTypes actual = underTest.toBuilder()
                    .doubleValue(Double.POSITIVE_INFINITY)
                    .build();
                callback.that(actual).hasDoubleValue(Double.NEGATIVE_INFINITY);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.doubleValue()");
            assertThat(error).factValue("expected").isEqualTo("-Infinity");
            assertThat(error).factValue("but was").isEqualTo("Infinity");
        }

        @Test
        void shouldFailAssertionWhenNull() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).hasDoubleValue(Double.POSITIVE_INFINITY);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("autoBoxedPrimitiveTypes.doubleValue()");
            assertThat(error).factValue("expected").isEqualTo("Infinity");
            assertThat(error).factValue("but was").isEqualTo("null");
        }
    }
    @Nonnull
    private static AssertionError expectFailure(
        @Nonnull SimpleSubjectBuilderCallback<AutoBoxedPrimitiveTypesSubject, BoxedPrimitiveTypes> callback) {
        return expectFailureAbout(BoxedPrimitiveTypesSubject.boxedPrimitiveTypes(), callback);
    }

    @AutoSubject(BoxedPrimitiveTypes.class)
    public static class BoxedPrimitiveTypesSubject {
        static Factory<AutoBoxedPrimitiveTypesSubject, BoxedPrimitiveTypes> boxedPrimitiveTypes() {
            return AutoBoxedPrimitiveTypesSubject::new;
        }
    }
}
