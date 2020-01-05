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
import com.google.common.truth.OptionalDoubleSubject;
import com.google.common.truth.OptionalIntSubject;
import com.google.common.truth.OptionalLongSubject;
import com.google.common.truth.OptionalSubject;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoOptionalValueObjectSubject extends Subject {
    private final OptionalValueObject actual;

    AutoOptionalValueObjectSubject(FailureMetadata failureMetadata, OptionalValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoOptionalValueObjectSubject, OptionalValueObject> optionalValueObject() {
        return AutoOptionalValueObjectSubject::new
    }

    public OptionalIntSubject intValue() {
        return check("getIntValue()").about(OptionalIntSubject.optionalInts()).that(actual.getIntValue());
    }

    public OptionalLongSubject longValue() {
        return check("getLongValue()").about(OptionalLongSubject.optionalLongs()).that(actual.getLongValue());
    }

    public OptionalDoubleSubject doubleValue() {
        return check("getDoubleValue()").about(OptionalDoubleSubject.optionalDoubles()).that(actual.getDoubleValue());
    }

    public OptionalSubject stringValue() {
        return check("getStringValue()").about(OptionalSubject.optionals()).that(actual.getStringValue());
    }

    public GuavaOptionalSubject value() {
        return check("getValue()").that(actual.getValue());
    }
}
