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

import java.util.List;

@AutoValue
public abstract class GenericTypes<T> {
    public abstract T value();

    public T[] valueArray() {
        return null;
    }

    public abstract List<T> valueList();

    public static <T> Builder<T> builder() {
        return new AutoValue_GenericTypes.Builder<>();
    }

    @AutoValue.Builder
    public static abstract class Builder<T> {
        public abstract Builder<T> value(T value);

        public abstract Builder<T> valueList(List<T> values);

        public abstract GenericTypes<T> build();
    }
}
