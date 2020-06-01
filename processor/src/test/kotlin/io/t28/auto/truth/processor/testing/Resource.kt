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

package io.t28.auto.truth.processor.testing

import com.google.testing.compile.JavaFileObjects.forResource
import java.io.File
import javax.tools.JavaFileObject

abstract class Resource {
    val packageName: String = "io.t28.auto.truth.test"

    val simpleName: String = "${this::class.simpleName}"

    val qualifiedName: String = "$packageName.$simpleName"

    companion object {
        private const val PACKAGE_SEPARATOR = '.'
        private const val JAVA_EXTENSION = ".java"
    }

    fun toJavaFileObject(): JavaFileObject {
        val path = packageName.replace(PACKAGE_SEPARATOR, File.separatorChar)
        return forResource("$path${File.separatorChar}$simpleName$JAVA_EXTENSION")
    }

    object AutoUserSubject : Resource()
    object AutoUserTypeSubject : Resource()
    object ArrayTypes : Resource()
    object ArrayTypesSubject : Resource()
    object BoxedPrimitiveTypes : Resource()
    object BoxedPrimitiveTypesSubject : Resource()
    object EmptyPrefixUserSubject : Resource()
    object EmptySuffixUserSubject : Resource()
    object EnumTypes : Resource()
    object EnumTypesSubject : Resource()
    object GuavaTypes : Resource()
    object GuavaTypesSubject : Resource()
    object InvalidPrefixUserSubject : Resource()
    object InvalidSuffixUserSubject : Resource()
    object IterableTypes : Resource()
    object IterableTypesSubject : Resource()
    object MapTypes : Resource()
    object MapTypesSubject : Resource()
    object OptionalTypes : Resource()
    object OptionalTypesSubject : Resource()
    object StreamTypes : Resource()
    object StreamTypesSubject : Resource()
    object User : Resource()
    object UserSubject : Resource()
    object UserTypeSubject : Resource()
    object PrimitiveTypes : Resource()
    object PrimitiveTypesSubject : Resource()
    object ValidPrefixUserSubject : Resource()
    object ValidSuffixUserSubject : Resource()
}
