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

import java.util.Arrays;

public class DeclaredArrayTypes {
    private final Object[] objects;
    private final String[] strings;

    private DeclaredArrayTypes(Builder builder) {
        this.objects = Arrays.copyOf(builder.objects, builder.objects.length);
        this.strings = Arrays.copyOf(builder.strings, builder.strings.length);
    }

    public Object[] objectArray() {
        return Arrays.copyOf(objects, objects.length);
    }

    public String[] stringArray() {
        return Arrays.copyOf(strings, strings.length);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object[] objects;
        private String[] strings;

        private Builder() {
            this.objects = new Object[0];
            this.strings = new String[0];
        }

        public Builder objects(Object... values) {
            this.objects = Arrays.copyOf(values, values.length);
            return this;
        }

        public Builder strings(String... values) {
            this.strings = Arrays.copyOf(values, values.length);
            return this;
        }

        public DeclaredArrayTypes build() {
            return new DeclaredArrayTypes(this);
        }
    }
}
