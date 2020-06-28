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

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PrimitiveArrayTypes {
    @SuppressWarnings("mutable")
    public abstract boolean[] booleanArray();

    @SuppressWarnings("mutable")
    public abstract byte[] byteArray();

    @SuppressWarnings("mutable")
    public abstract short[] shortArray();

    @SuppressWarnings("mutable")
    public abstract int[] intArray();

    @SuppressWarnings("mutable")
    public abstract long[] longArray();

    @SuppressWarnings("mutable")
    public abstract char[] charArray();

    @SuppressWarnings("mutable")
    public abstract float[] floatArray();

    @SuppressWarnings("mutable")
    public abstract double[] doubleArray();

    public static Builder builder() {
        return new AutoValue_PrimitiveArrayTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder booleanArray(boolean... values);

        public abstract Builder byteArray(byte... values);

        public abstract Builder charArray(char... values);

        public abstract Builder shortArray(short... values);

        public abstract Builder intArray(int... values);

        public abstract Builder longArray(long... values);

        public abstract Builder floatArray(float... values);

        public abstract Builder doubleArray(double... values);

        public abstract PrimitiveArrayTypes build();
    }
}
