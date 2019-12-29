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
import com.google.common.truth.PrimitiveBooleanArraySubject;
import com.google.common.truth.PrimitiveByteArraySubject;
import com.google.common.truth.PrimitiveCharArraySubject;
import com.google.common.truth.PrimitiveDoubleArraySubject;
import com.google.common.truth.PrimitiveFloatArraySubject;
import com.google.common.truth.PrimitiveIntArraySubject;
import com.google.common.truth.PrimitiveLongArraySubject;
import com.google.common.truth.PrimitiveShortArraySubject;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoPrimitiveArrayValueObjectSubject extends Subject {
    private final PrimitiveArrayValueObject actual;

    AutoPrimitiveArrayValueObjectSubject(FailureMetadata failureMetadata, PrimitiveArrayValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoPrimitiveArrayValueObjectSubject, PrimitiveArrayValueObject> primitiveArrayValueObject() {
        return AutoPrimitiveArrayValueObjectSubject::new
    }

    public PrimitiveBooleanArraySubject booleans() {
        return check("getBooleans()").that(this.actual.getBooleans());
    }

    public PrimitiveByteArraySubject bytes() {
        return check("getBytes()").that(this.actual.getBytes());
    }

    public PrimitiveShortArraySubject shorts() {
        return check("getShorts()").that(this.actual.getShorts());
    }

    public PrimitiveIntArraySubject ints() {
        return check("getInts()").that(this.actual.getInts());
    }

    public PrimitiveLongArraySubject longs() {
        return check("getLongs()").that(this.actual.getLongs());
    }

    public PrimitiveCharArraySubject chars() {
        return check("getChars()").that(this.actual.getChars());
    }

    public PrimitiveFloatArraySubject floats() {
        return check("getFloats()").that(this.actual.getFloats());
    }

    public PrimitiveDoubleArraySubject doubles() {
        return check("getDoubles()").that(this.actual.getDoubles());
    }
}
