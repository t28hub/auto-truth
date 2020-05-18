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

package io.t28.auto.truth.processor.util

fun <T1, T2> fixture(value1: T1, value2: T2): Fixture2<T1, T2> = Fixture2(value1, value2)

fun <T1, T2, T3> fixture(value1: T1, value2: T2, value3: T3): Fixture3<T1, T2, T3> = Fixture3(value1, value2, value3)

fun <T1, T2, T3, T4> fixture(value1: T1, value2: T2, value3: T3, value4: T4): Fixture4<T1, T2, T3, T4> = Fixture4(value1, value2, value3, value4)
