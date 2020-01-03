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

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.GuavaOptionalSubject;
import com.google.common.truth.MultimapSubject;
import com.google.common.truth.MultisetSubject;
import com.google.common.truth.Subject;
import com.google.common.truth.TableSubject;
import java.util.Arrays;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoGuavaValueObjectSubject extends Subject {
    private final GuavaValueObject actual;

    AutoGuavaValueObjectSubject(FailureMetadata failureMetadata, GuavaValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoGuavaValueObjectSubject, GuavaValueObject> guavaValueObject() {
        return AutoGuavaValueObjectSubject::new
    }

    public GuavaOptionalSubject value() {
        return check("getValue()").that(actual.getValue());
    }

    public MultisetSubject multiset() {
        return check("getMultiset()").that(actual.getMultiset());
    }

    public void hasMultiset(String... expected) {
        check("getMultiset()").that(actual.getMultiset()).containsAtLeastElementsIn(Arrays.asList(expected));
    }

    public void doesNotHaveMultiset(String... expected) {
        check("getMultiset()").that(actual.getMultiset()).containsNoneIn(Arrays.asList(expected));
    }

    public MultimapSubject multimap() {
        return check("getMultimap()").that(actual.getMultimap());
    }

    public TableSubject table() {
        return check("getTable()").that(actual.getTable());
    }
}
