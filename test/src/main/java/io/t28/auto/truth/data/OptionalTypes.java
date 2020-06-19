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

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

@AutoValue
public abstract class OptionalTypes {
    public abstract OptionalInt optionalInt();

    public abstract OptionalLong optionalLong();

    public abstract OptionalDouble optionalDouble();

    public abstract Optional<String> optionalString();

    public static Builder builder() {
        return new AutoValue_OptionalTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder optionalInt(int value);

        public abstract Builder optionalLong(long value);

        public abstract Builder optionalDouble(double value);

        public abstract Builder optionalString(String value);

        public abstract OptionalTypes build();
    }
}
