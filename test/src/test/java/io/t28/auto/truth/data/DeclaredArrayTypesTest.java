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

import com.google.common.truth.ObjectArraySubject;
import com.google.common.truth.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.ExpectFailure.assertThat;
import static com.google.common.truth.Truth.assertThat;
import static io.t28.auto.truth.data.DeclaredArrayTypesSubject.assertThat;
import static io.t28.auto.truth.data.DeclaredArrayTypesSubject.expectFailure;

class DeclaredArrayTypesTest {
    private DeclaredArrayTypes underTest;

    @BeforeEach
    void setup() {
        underTest = DeclaredArrayTypes.builder()
            .objects(Short.class, Integer.class, Long.class)
            .strings("foo", "bar")
            .build();
    }

    @Test
    void shouldReturnObjectArraySubject() {
        // Act
        final Subject objectArraySubject = assertThat(underTest).objectArray();
        final Subject stringArraySubject = assertThat(underTest).stringArray();

        // Assert
        assertThat(objectArraySubject).isInstanceOf(ObjectArraySubject.class);
        assertThat(stringArraySubject).isInstanceOf(ObjectArraySubject.class);
    }

    @Test
    void shouldPassAssertion() {
        // Assert
        assertThat(underTest).stringArray().hasLength(2);
        assertThat(underTest).stringArray().asList().containsExactly("foo", "bar");
    }

    @Test
    void shouldFailAssertion() {
        // Act
        final AssertionError error = expectFailure(callback -> {
            callback.that(underTest).objectArray().asList().containsExactly(Short.class, Integer.class);
        });

        // Assert
        assertThat(error).factValue("value of").isEqualTo("declaredArrayTypes.objectArray().asList()");
        assertThat(error).factValue("expected", 0).isEqualTo("[class java.lang.Short, class java.lang.Integer]");
        assertThat(error).factValue("but was", 0).isEqualTo("[class java.lang.Short, class java.lang.Integer, class java.lang.Long]");
    }
}
