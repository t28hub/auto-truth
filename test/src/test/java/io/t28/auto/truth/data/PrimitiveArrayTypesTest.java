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
import com.google.common.truth.PrimitiveBooleanArraySubject;
import com.google.common.truth.PrimitiveByteArraySubject;
import com.google.common.truth.PrimitiveCharArraySubject;
import com.google.common.truth.PrimitiveDoubleArraySubject;
import com.google.common.truth.PrimitiveFloatArraySubject;
import com.google.common.truth.PrimitiveIntArraySubject;
import com.google.common.truth.PrimitiveLongArraySubject;
import com.google.common.truth.PrimitiveShortArraySubject;
import com.google.common.truth.Subject;
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
import static io.t28.auto.truth.data.PrimitiveArrayTypesTest.PrimitiveArrayTypesSubject.assertThat;
import static io.t28.auto.truth.data.PrimitiveArrayTypesTest.PrimitiveArrayTypesSubject.expectFailure;

class PrimitiveArrayTypesTest {
    private PrimitiveArrayTypes underTest;

    @BeforeEach
    void setup() {
        underTest = PrimitiveArrayTypes.builder()
            .booleanArray(true, false, true)
            .byteArray((byte) 1, (byte) 2)
            .charArray('a', 'b', 'c')
            .shortArray((short) 3, (short) 4, (short) 5)
            .intArray(6)
            .longArray(7L, 8L)
            .floatArray(1.23f, 2.34f, 3.45f)
            .doubleArray(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
            .build();
    }

    @Nested
    class PrimitiveBooleanArray {
        @Test
        void shouldReturnPrimitiveBooleanArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).booleanArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveBooleanArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).booleanArray().isEqualTo(new boolean[]{true, false, true});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).booleanArray().isEqualTo(new boolean[]{false, true});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.booleanArray()");
            assertThat(error).factValue("expected", 0).isEqualTo("[false, true]");
            assertThat(error).factValue("but was", 0).isEqualTo("[true, false, true]");
        }
    }

    @Nested
    class PrimitiveByteArray {
        @Test
        void shouldReturnPrimitiveByteArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).byteArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveByteArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).byteArray().isEqualTo(new byte[]{1, 2});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).byteArray().isEqualTo(new byte[]{1});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.byteArray()");
            assertThat(error).factValue("expected", 0).isEqualTo("01");
            assertThat(error).factValue("but was", 0).isEqualTo("0102");
        }
    }

    @Nested
    class PrimitiveCharArray {
        @Test
        void shouldReturnPrimitiveCharArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).charArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveCharArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).charArray().isEqualTo(new char[]{'a', 'b', 'c'});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).charArray().isEqualTo(new char[]{'a', 'c', 'b'});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.charArray()");
            assertThat(error).factValue("expected", 0).isEqualTo("[a, c, b]");
            assertThat(error).factValue("but was", 0).isEqualTo("[a, b, c]");
        }
    }

    @Nested
    class PrimitiveShortArray {
        @Test
        void shouldReturnPrimitiveShortArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).shortArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveShortArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).shortArray().isEqualTo(new short[]{3, 4, 5});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).shortArray().isEqualTo(new short[]{4, 5, 6});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.shortArray()");
            assertThat(error).factValue("expected").isEqualTo("[4, 5, 6]");
            assertThat(error).factValue("but was").isEqualTo("[3, 4, 5]");
        }
    }

    @Nested
    class PrimitiveIntArray {
        @Test
        void shouldReturnPrimitiveIntArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).intArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveIntArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).intArray().isEqualTo(new int[]{6});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).intArray().isEmpty();
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.intArray()");
            assertThat(error).factKeys().contains("expected to be empty");
            assertThat(error).factValue("but was").isEqualTo("[6]");
        }
    }

    @Nested
    class PrimitiveLongArray {
        @Test
        void shouldReturnPrimitiveLongArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).longArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveLongArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).longArray().isEqualTo(new long[]{7, 8});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).longArray().isEqualTo(new long[]{7, 8, 9});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.longArray()");
            assertThat(error).factValue("expected", 0).isEqualTo("[7, 8, 9]");
            assertThat(error).factValue("but was", 0).isEqualTo("[7, 8]");
        }
    }

    @Nested
    class PrimitiveFloatArray {
        @Test
        void shouldReturnPrimitiveFloatArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).floatArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveFloatArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).floatArray().hasLength(3);
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).floatArray().hasLength(2);
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.floatArray().length");
            assertThat(error).factValue("expected").isEqualTo("2");
            assertThat(error).factValue("but was").isEqualTo("3");
        }
    }

    @Nested
    class PrimitiveDoubleArray {
        @Test
        void shouldReturnPrimitiveDoubleArraySubject() {
            // Act
            final Subject subject = assertThat(underTest).doubleArray();

            // Assert
            assertThat(subject).isInstanceOf(PrimitiveDoubleArraySubject.class);
        }

        @Test
        void shouldPassAssertion() {
            // Assert
            assertThat(underTest).doubleArray().isEqualTo(new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY});
        }

        @Test
        void shouldFailAssertion() {
            // Act
            final AssertionError error = expectFailure(callback -> {
                callback.that(underTest).doubleArray().isEqualTo(new double[]{Double.NaN});
            });

            // Assert
            assertThat(error).factValue("value of").isEqualTo("primitiveArrayTypes.doubleArray()");
            assertThat(error).factValue("expected", 0).isEqualTo("[NaN]");
            assertThat(error).factValue("but was", 0).isEqualTo("[-Infinity, Infinity]");
        }
    }

    @AutoSubject(PrimitiveArrayTypes.class)
    public static class PrimitiveArrayTypesSubject extends AutoPrimitiveArrayTypesSubject {
        protected PrimitiveArrayTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable PrimitiveArrayTypes actual) {
            super(failureMetadata, actual);
        }

        @Nonnull
        @CheckReturnValue
        public static PrimitiveArrayTypesSubject assertThat(@Nullable PrimitiveArrayTypes actual) {
            return assertAbout(PrimitiveArrayTypesSubject::new).that(actual);
        }

        @Nonnull
        public static AssertionError expectFailure(
            @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<PrimitiveArrayTypesSubject, PrimitiveArrayTypes> callback) {
            return expectFailureAbout(PrimitiveArrayTypesSubject::new, callback);
        }
    }
}
