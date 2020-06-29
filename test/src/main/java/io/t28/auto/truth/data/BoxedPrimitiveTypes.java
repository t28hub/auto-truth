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

import javax.annotation.Nullable;

@AutoValue
public abstract class BoxedPrimitiveTypes {
    @Nullable
    public abstract Boolean booleanValue();

    @Nullable
    public abstract Byte byteValue();

    @Nullable
    public abstract Character characterValue();

    @Nullable
    public abstract Short shortValue();

    @Nullable
    public abstract Integer integerValue();

    @Nullable
    public abstract Long longValue();

    @Nullable
    public abstract Float floatValue();

    @Nullable
    public abstract Double doubleValue();

    public Builder toBuilder() {
        return new AutoValue_BoxedPrimitiveTypes.Builder()
            .booleanValue(booleanValue())
            .byteValue(byteValue())
            .characterValue(characterValue())
            .shortValue(shortValue())
            .integerValue(integerValue())
            .longValue(longValue())
            .floatValue(floatValue())
            .doubleValue(doubleValue());
    }

    public static Builder builder() {
        return new AutoValue_BoxedPrimitiveTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder booleanValue(Boolean value);

        public abstract Builder byteValue(Byte value);

        public abstract Builder characterValue(Character value);

        public abstract Builder shortValue(Short value);

        public abstract Builder integerValue(Integer value);

        public abstract Builder longValue(Long value);

        public abstract Builder floatValue(Float value);

        public abstract Builder doubleValue(Double value);

        public abstract BoxedPrimitiveTypes build();
    }
}
