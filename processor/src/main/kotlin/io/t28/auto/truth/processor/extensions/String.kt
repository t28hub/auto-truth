/*
 * Copyright 2019 Tatsuya Maki
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

package io.t28.auto.truth.processor.extensions

import java.util.IllegalFormatException

fun String.isValidClassPrefix(): Boolean {
    if (isEmpty()) {
        return true
    }
    if (!Character.isJavaIdentifierStart(this[0])) {
        return false
    }
    return drop(1).all(Character::isJavaIdentifierPart)
}

fun String.isValidClassSuffix(): Boolean {
    if (isEmpty()) {
        return true
    }
    return all(Character::isJavaIdentifierPart)
}

fun String.safeFormat(vararg args: Any?): String {
    return try {
        format(*args)
    } catch (e: IllegalFormatException) {
        this
    }
}
