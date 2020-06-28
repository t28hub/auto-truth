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

import com.google.common.truth.ClassSubject;
import com.google.common.truth.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.ClassTypesSubject.assertThat;
import static io.t28.auto.truth.data.ClassTypesSubject.expectFailure;

class ClassTypesTest {
    private ClassTypes underTest;

    @BeforeEach
    void setup() {
        underTest = ClassTypes.builder()
            .anyClass(Class.class)
            .objectClass(Object.class)
            .stringClass(String.class)
            .build();
    }

    @Test
    void shouldReturnClassSubject() {
        // Act
        final Subject anyClassSubject = assertThat(underTest).anyClass();
        final Subject objectClassSubject = assertThat(underTest).objectClass();
        final Subject stringClassSubject = assertThat(underTest).stringClass();

        // Assert
        assertThat(anyClassSubject).isInstanceOf(ClassSubject.class);
        assertThat(objectClassSubject).isInstanceOf(ClassSubject.class);
        assertThat(stringClassSubject).isInstanceOf(ClassSubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).anyClass().isAssignableTo(Object.class);
        assertThat(underTest).objectClass().isAssignableTo(Object.class);
        assertThat(underTest).stringClass().isAssignableTo(CharSequence.class);
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).objectClass().isAssignableTo(String.class);
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("classTypes.objectClass()");
        assertThat(error).factValue("expected to be assignable to").isEqualTo("java.lang.String");
        assertThat(error).factValue("but was").isEqualTo("class java.lang.Object");
    }
}
