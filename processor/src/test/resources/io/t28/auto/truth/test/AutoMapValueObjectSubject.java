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
import com.google.common.truth.MapSubject;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoMapValueObjectSubject extends Subject {
    private final MapValueObject actual;

    AutoMapValueObjectSubject(FailureMetadata failureMetadata, MapValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoMapValueObjectSubject, MapValueObject> mapValueObject() {
        return AutoMapValueObjectSubject::new;
    }

    public MapSubject map() {
        return check("getMap()").that(actual.getMap());
    }

    public MapSubject hashMap() {
        return check("getHashMap()").that(actual.getHashMap());
    }

    public MapSubject enumMap() {
        return check("getEnumMap()").that(actual.getEnumMap());
    }

    public MapSubject customMap() {
        return check("getCustomMap()").that(actual.getCustomMap());
    }
}
