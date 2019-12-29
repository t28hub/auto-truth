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
public class AutoPrimitiveValueObjectSubject extends Subject {
    private final PrimitiveValueObject actual;

    AutoPrimitiveValueObjectSubject(FailureMetadata failureMetadata, PrimitiveValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoPrimitiveValueObjectSubject, PrimitiveValueObject> primitiveValueObject() {
        return AutoPrimitiveValueObjectSubject::new
    }

    public void isBooleanValue() {
        if (!this.actual.isBooleanValue()) {
            failWithActual(Fact.simpleFact("expected to be booleanValue"));
        }
    }

    public void isNotBooleanValue() {
        if (this.actual.isBooleanValue()) {
            failWithActual(Fact.simpleFact("expected not to be booleanValue"));
        }
    }

    public void hasByteValue(byte expected) {
        check("getByteValue()").that(this.actual.getByteValue()).isEqualTo(expected);
    }

    public void hasShortValue(short expected) {
        check("getShortValue()").that(this.actual.getShortValue()).isEqualTo(expected);
    }

    public void hasIntValue(int expected) {
        check("getIntValue()").that(this.actual.getIntValue()).isEqualTo(expected);
    }

    public void hasLongValue(long expected) {
        check("getLongValue()").that(this.actual.getLongValue()).isEqualTo(expected);
    }

    public void hasCharValue(char expected) {
        check("getCharValue()").that(this.actual.getCharValue()).isEqualTo(expected);
    }

    public void hasFloatValue(float expected) {
        check("getFloatValue()").that(this.actual.getFloatValue()).isEqualTo(expected);
    }

    public void hasDooubleValue(double expected) {
        check("getDooubleValue()").that(this.actual.getDooubleValue()).isEqualTo(expected);
    }
}
