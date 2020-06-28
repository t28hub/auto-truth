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

import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback;
import com.google.common.truth.FailureMetadata;
import io.t28.auto.truth.AutoSubject;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.truth.ExpectFailure.expectFailureAbout;
import static com.google.common.truth.Truth.assertAbout;

@AutoSubject(ClassTypes.class)
public class ClassTypesSubject extends AutoClassTypesSubject {
    protected ClassTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable ClassTypes actual) {
        super(failureMetadata, actual);
    }

    @Nonnull
    @CheckReturnValue
    public static ClassTypesSubject assertThat(@Nullable ClassTypes actual) {
        return assertAbout(ClassTypesSubject::new).that(actual);
    }

    @Nonnull
    public static AssertionError expectFailure(
        @Nonnull SimpleSubjectBuilderCallback<ClassTypesSubject, ClassTypes> callback) {
        return expectFailureAbout(ClassTypesSubject::new, callback);
    }
}
