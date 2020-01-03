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

package io.t28.auto.truth.test;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Table;
import io.t28.auto.truth.AutoSubject;

@AutoSubject
public class GuavaValueObject {
    private final String value;
    private final Multiset<String> multiset;
    private final Multimap<String, Object> multimap;
    private final Table<String, String, Integer> table;

    public GuavaValueObject(String value, Multiset<String> multiset, Multimap<String, Object> multimap, Table<String, String, Integer> table) {
        this.value = value;
        this.multiset = multiset;
        this.multimap = multimap;
        this.table = table;
    }

    public Optional<String> getValue() {
        return Optional.fromNullable(value);
    }

    public Multiset<String> getMultiset() {
        return multiset;
    }

    public Multimap<String, Object> getMultimap() {
        return multimap;
    }

    public Table<String, String, Integer> getTable() {
        return table;
    }
}
