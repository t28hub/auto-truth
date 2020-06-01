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

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import com.google.common.truth.Truth;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoUserTypeSubject extends Subject {
    private final User.Type actual;

    protected AutoUserTypeSubject(@Nonnull FailureMetadata failureMetadata, @Nullable User.Type actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    @Nonnull
    public static AutoUserTypeSubject assertThat(@Nullable User.Type actual) {
        return Truth.assertAbout(type()).that(actual);
    }

    @Nonnull
    public static Subject.Factory<AutoUserTypeSubject, User.Type> type() {
        return AutoUserTypeSubject::new;
    }

    public void isGuest() {
        if (actual != User.Type.GUEST) {
            failWithActual("expected to be GUEST", actual);
        }
    }

    public void isNotGuest() {
        if (actual == User.Type.GUEST) {
            failWithActual("expected not to be GUEST", actual);
        }
    }

    public void isOwner() {
        if (actual != User.Type.OWNER) {
            failWithActual("expected to be OWNER", actual);
        }
    }

    public void isNotOwner() {
        if (actual == User.Type.OWNER) {
            failWithActual("expected not to be OWNER", actual);
        }
    }
}
