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
public abstract class PrimitiveTypes {
    public abstract boolean booleanValue();

    public abstract byte byteValue();

    public abstract char charValue();

    public abstract short shortValue();

    public abstract int intValue();

    public abstract long longValue();

    public abstract float floatValue();

    public abstract double doubleValue();

    public Builder toBuilder() {
        return new AutoValue_PrimitiveTypes.Builder()
            .booleanValue(booleanValue())
            .byteValue(byteValue())
            .charValue(charValue())
            .shortValue(shortValue())
            .intValue(intValue())
            .longValue(longValue())
            .floatValue(floatValue())
            .doubleValue(doubleValue());
    }

    public static Builder builder() {
        return new AutoValue_PrimitiveTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder booleanValue(boolean value);

        public abstract Builder byteValue(byte value);

        public abstract Builder charValue(char value);

        public abstract Builder shortValue(short value);

        public abstract Builder intValue(int value);

        public abstract Builder longValue(long value);

        public abstract Builder floatValue(float value);

        public abstract Builder doubleValue(double value);

        public abstract PrimitiveTypes build();
    }
}
