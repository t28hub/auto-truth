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

import com.google.common.truth.Fact;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoUserSubject extends Subject {
    private final User actual;

    AutoUserSubject(FailureMetadata failureMetadata, User actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoUserSubject, User> user() {
        return AutoUserSubject::new
    }

    public void hasId(long expected) {
        check("getId()").that(this.actual.getId()).isEqualTo(expected);
    }

    public void hasName(String expected) {
        check("getName()").that(this.actual.getName()).isEqualTo(expected);
    }

    public void hasAge(int expected) {
        check("getAge()").that(this.actual.getAge()).isEqualTo(expected);
    }

    public void isAdmin() {
        if (!this.actual.isAdmin()) {
            failWithActual(Fact.simpleFact("expected to be admin"));
        }
    }

    public void isNotAdmin() {
        if (this.actual.isAdmin()) {
            failWithActual(Fact.simpleFact("expected not to be admin"));
        }
    }
}
