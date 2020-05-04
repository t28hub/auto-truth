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
import com.google.common.truth.IntStreamSubject;
import com.google.common.truth.LongStreamSubject;
import com.google.common.truth.StreamSubject;
import com.google.common.truth.Subject;
import javax.annotation.Generated;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoStreamValueObjectSubject extends Subject {
    private final StreamValueObject actual;

    AutoStreamValueObjectSubject(FailureMetadata failureMetadata, StreamValueObject actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static Subject.Factory<AutoStreamValueObjectSubject, StreamValueObject> streamValueObject() {
        return AutoStreamValueObjectSubject::new;
    }

    public StreamSubject stringStream() {
        return check("getStringStream()").about(StreamSubject.streams()).that(actual.getStringStream());
    }

    public IntStreamSubject intStream() {
        return check("getIntStream()").about(IntStreamSubject.intStreams()).that(actual.getIntStream());
    }

    public LongStreamSubject longStream() {
        return check("getLongStream()").about(LongStreamSubject.longStreams()).that(actual.getLongStream());
    }
}
