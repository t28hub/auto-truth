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

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@AutoSubject
public class StreamValueObject {
    public Stream<String> getStringStream() {
        return Stream.of("Alice", "Bob", "Charlie");
    }

    public IntStream getIntStream() {
        return IntStream.of(1, 2, 3);
    }

    public LongStream getLongStream() {
        return LongStream.of(23L, 42L);
    }
}
