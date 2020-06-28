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

package io.t28.auto.truth.data;

import com.google.common.truth.ExpectFailure;
import com.google.common.truth.FailureMetadata;
import io.t28.auto.truth.AutoSubject;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;

@AutoSubject(GuavaTypes.class)
public class GuavaTypesSubject extends AutoGuavaTypesSubject {
    protected GuavaTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable GuavaTypes actual) {
        super(failureMetadata, actual);
    }

    @Nonnull
    @CheckReturnValue
    public static GuavaTypesSubject assertThat(@Nullable GuavaTypes actual) {
        return assertAbout(GuavaTypesSubject::new).that(actual);
    }

    @Nonnull
    public static AssertionError expectFailure(
        @Nonnull ExpectFailure.SimpleSubjectBuilderCallback<GuavaTypesSubject, GuavaTypes> callback) {
        return expectFailureAbout(GuavaTypesSubject::new, callback);
    }
}
