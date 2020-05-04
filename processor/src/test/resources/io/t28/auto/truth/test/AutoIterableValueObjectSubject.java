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

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.IterableSubject;
import com.google.common.truth.Subject;
import java.util.Arrays;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoIterableValueObjectSubject extends Subject {
    private final IterableValueObject actual;

    AutoIterableValueObjectSubject(FailureMetadata failureMetadata, IterableValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoIterableValueObjectSubject, IterableValueObject> iterableValueObject() {
        return AutoIterableValueObjectSubject::new;
    }

    public IterableSubject iterable() {
        return check("getIterable()").that(actual.getIterable());
    }

    public void hasIterable(Object... expected) {
        check("getIterable()").that(actual.getIterable()).containsAtLeastElementsIn(Arrays.asList(expected));
    }

    public void doesNotHaveIterable(Object... expected) {
        check("getIterable()").that(actual.getIterable()).containsNoneIn(Arrays.asList(expected));
    }

    public IterableSubject collection() {
        return check("getCollection()").that(actual.getCollection());
    }

    public void hasCollection(String... expected) {
        check("getCollection()").that(actual.getCollection()).containsAtLeastElementsIn(Arrays.asList(expected));
    }

    public void doesNotHaveCollection(String... expected) {
        check("getCollection()").that(actual.getCollection()).containsNoneIn(Arrays.asList(expected));
    }

    public IterableSubject list() {
        return check("getList()").that(actual.getList());
    }

    public void hasList(Integer... expected) {
        check("getList()").that(actual.getList()).containsAtLeastElementsIn(Arrays.asList(expected));
    }

    public void doesNotHaveList(Integer... expected) {
        check("getList()").that(actual.getList()).containsNoneIn(Arrays.asList(expected));
    }

    public IterableSubject customList() {
        return check("getCustomList()").that(actual.getCustomList());
    }
}
