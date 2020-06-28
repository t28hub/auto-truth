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

@AutoSubject(StreamTypes.class)
public class StreamTypesSubject extends AutoStreamTypesSubject {
    protected StreamTypesSubject(@Nonnull FailureMetadata failureMetadata, @Nullable StreamTypes actual) {
        super(failureMetadata, actual);
    }

    @Nonnull
    @CheckReturnValue
    public static StreamTypesSubject assertThat(@Nullable StreamTypes actual) {
        return assertAbout(StreamTypesSubject::new).that(actual);
    }

    @Nonnull
    public static AssertionError expectFailure(
        @Nonnull SimpleSubjectBuilderCallback<StreamTypesSubject, StreamTypes> callback) {
        return expectFailureAbout(StreamTypesSubject::new, callback);
    }
}
