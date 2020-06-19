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

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@AutoValue
public abstract class StreamTypes {
    public abstract IntStream intStream();

    public abstract LongStream longStream();

    public abstract DoubleStream doubleStream();

    public abstract Stream<String> stringStream();

    public static Builder builder() {
        return new AutoValue_StreamTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder intStream(IntStream value);

        public abstract Builder longStream(LongStream value);

        public abstract Builder doubleStream(DoubleStream value);

        public abstract Builder stringStream(Stream<String> value);

        public abstract StreamTypes build();
    }
}
