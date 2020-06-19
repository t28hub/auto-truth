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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.truth.ExpectFailure.assertThat;
import static io.t28.auto.truth.data.EnumTypesSubject.assertThat;
import static io.t28.auto.truth.data.EnumTypesSubject.expectFailure;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class EnumTypesTest {
    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(EnumTypes.FOO).isFoo();
        assertThat(EnumTypes.FOO).isNotBar();
        assertThat(EnumTypes.BAR).isBar();
        assertThat(EnumTypes.BAR).isNotFoo();
    }

    @ParameterizedTest
    @MethodSource("failureAssertionProvider")
    void shouldFailAssertion(EnumTypes actual, Consumer<EnumTypesSubject> assertion, String expected) {
        // Act
        final AssertionError error = expectFailure(callback -> {
            final EnumTypesSubject subject = callback.that(actual);
            assertion.accept(subject);
        });

        // Assert
        assertThat(error).factKeys().contains(expected);
        assertThat(error).factValue("but was").isEqualTo(actual.name());
    }

    static Stream<Arguments> failureAssertionProvider() {
        return Stream.of(
            arguments(EnumTypes.FOO, (Consumer<EnumTypesSubject>) AutoEnumTypesSubject::isNotFoo, "expected not to be FOO"),
            arguments(EnumTypes.FOO, (Consumer<EnumTypesSubject>) AutoEnumTypesSubject::isBar, "expected to be BAR"),
            arguments(EnumTypes.BAR, (Consumer<EnumTypesSubject>) AutoEnumTypesSubject::isFoo, "expected to be FOO"),
            arguments(EnumTypes.BAR, (Consumer<EnumTypesSubject>) AutoEnumTypesSubject::isNotBar, "expected not to be BAR")
        );
    }
}
