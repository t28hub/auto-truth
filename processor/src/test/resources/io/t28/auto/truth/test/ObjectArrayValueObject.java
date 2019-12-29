/*
 * Copyright 2019 Tatsuya Maki
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

package io.t28.auto.truth.test;

import io.t28.auto.truth.AutoSubject;

import java.util.Arrays;

@AutoSubject
public class ObjectArrayValueObject {
    private final Object[] objects;
    private final String[] strings;

    public ObjectArrayValueObject(Object[] objects, String[] strings) {
        this.objects = Arrays.copyOf(objects, objects.length);
        this.strings = Arrays.copyOf(strings, strings.length);
    }

    public Object[] getObjects() {
        return objects;
    }

    public String[] getStrings() {
        return strings;
    }
}
