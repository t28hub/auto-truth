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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AutoSubject
public class IterableValueObject {
    private final Iterable<Object> iterable;
    private final Collection<String> collection;
    private final List<Integer> list;
    private final CustomList customList;

    public IterableValueObject(Iterable<Object> iterable, Collection<String> collection, List<Integer> list, CustomList customList) {
        this.iterable = iterable;
        this.collection = collection;
        this.list = list;
        this.customList = customList;
    }

    public Iterable<Object> getIterable() {
        return iterable;
    }

    public Collection<String> getCollection() {
        return collection;
    }

    public List<Integer> getList() {
        return list;
    }

    public CustomList getCustomList() {
        return customList;
    }

    public static class CustomList extends ArrayList<String> {
    }
}
