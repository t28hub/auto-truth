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

import io.t28.auto.truth.AutoSubject;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

@AutoSubject
public class OptionalValueObject {
    private final int intValue;
    private final long longValue;
    private final double doubleValue;
    private final String stringValue;

    public OptionalValueObject(int intValue, long longValue, double doubleValue, String stringValue) {
        this.intValue = intValue;
        this.longValue = longValue;
        this.doubleValue = doubleValue;
        this.stringValue = stringValue;
    }

    public OptionalInt getIntValue() {
        return OptionalInt.of(intValue);
    }

    public OptionalLong getLongValue() {
        return OptionalLong.of(longValue);
    }

    public OptionalDouble getDoubleValue() {
        return OptionalDouble.of(doubleValue);
    }

    public Optional<String> getStringValue() {
        return Optional.of(stringValue);
    }

    public com.google.common.base.Optional<String> getValue() {
        return com.google.common.base.Optional.absent();
    }
}
