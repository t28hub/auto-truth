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

import com.google.common.truth.ClassSubject;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoClassValueObjectSubject extends Subject {
    private final ClassValueObject actual;

    AutoClassValueObjectSubject(FailureMetadata failureMetadata, ClassValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoClassValueObjectSubject, ClassValueObject> classValueObject() {
        return AutoClassValueObjectSubject::new;
    }

    public ClassSubject anyClass() {
        return check("getAnyClass()").that(actual.getAnyClass());
    }

    public ClassSubject objectClass() {
        return check("getObjectClass()").that(actual.getObjectClass());
    }

    public ClassSubject stringClass() {
        return check("getStringClass()").that(actual.getStringClass());
    }
}
