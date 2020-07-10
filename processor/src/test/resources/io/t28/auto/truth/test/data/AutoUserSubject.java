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

package io.t28.auto.truth.test.data;

import com.google.common.base.Preconditions;
import com.google.common.truth.Fact;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import com.google.common.truth.Truth;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoUserSubject extends Subject {
    private final User actual;

    protected AutoUserSubject(@Nonnull FailureMetadata failureMetadata, @Nullable User actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    @Nonnull
    public static AutoUserSubject assertThat(@Nullable User actual) {
        return Truth.assertAbout(AutoUserSubject::new).that(actual);
    }

    public void hasId(long expected) {
        final long actual = Preconditions.checkNotNull(this.actual).getId();
        check("getId()").that(actual).isEqualTo(expected);
    }

    public void hasName(String expected) {
        final String actual = Preconditions.checkNotNull(this.actual).getName();
        check("getName()").that(actual).isEqualTo(expected);
    }

    public void hasAge(int expected) {
        final int actual = Preconditions.checkNotNull(this.actual).getAge();
        check("getAge()").that(actual).isEqualTo(expected);
    }

    public void isAdmin() {
        Preconditions.checkNotNull(actual);
        if (!actual.isAdmin()) {
            failWithActual(Fact.simpleFact("expected to be admin"));
        }
    }

    public void isNotAdmin() {
        Preconditions.checkNotNull(actual);
        if (actual.isAdmin()) {
            failWithActual(Fact.simpleFact("expected not to be admin"));
        }
    }

    public void hasType(User.Type expected) {
        final User.Type actual = Preconditions.checkNotNull(this.actual).getType();
        check("getType()").that(actual).isEqualTo(expected);
    }
}
