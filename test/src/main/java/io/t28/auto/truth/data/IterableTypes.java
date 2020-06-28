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

import java.util.Collection;
import java.util.List;
import java.util.Set;

@AutoValue
public abstract class IterableTypes {
    public abstract Iterable<Object> iterable();

    public abstract Collection<String> collection();

    public abstract List<Integer> list();

    public abstract Set<String> set();

    public static Builder builder() {
        return new AutoValue_IterableTypes.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder iterable(Iterable<Object> value);

        public abstract Builder collection(Collection<String> value);

        public abstract Builder list(List<Integer> value);

        public abstract Builder set(Set<String> value);

        public abstract IterableTypes build();
    }
}
