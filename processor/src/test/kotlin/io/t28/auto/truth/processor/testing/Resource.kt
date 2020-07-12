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
import com.google.testing.compile.JavaFileObjects.forSourceString
import java.io.File
import javax.tools.JavaFileObject

abstract class Resource(val packageName: String = "io.t28.auto.truth.test") {
    val simpleName: String = "${this::class.simpleName}"

    val qualifiedName: String = "$packageName.$simpleName"

    companion object {
        private const val PACKAGE_SEPARATOR = '.'
        private const val JAVA_EXTENSION = ".java"
    }

    open fun toJavaFileObject(): JavaFileObject {
        val path = packageName.replace(PACKAGE_SEPARATOR, File.separatorChar)
        return forResource("$path${File.separatorChar}$simpleName$JAVA_EXTENSION")
    }

    object AbstractClass : Resource("io.t28.auto.truth.test.type")
    object AbstractClassSubject : Resource("io.t28.auto.truth.test.type")
    object EnumType : Resource("io.t28.auto.truth.test.type")
    object EnumTypeSubject : Resource("io.t28.auto.truth.test.type")
    object InterfaceType : Resource("io.t28.auto.truth.test.type")
    object InterfaceTypeSubject : Resource("io.t28.auto.truth.test.type")
    object NestedClass : Resource("io.t28.auto.truth.test.type")
    object NestedClassSubject : Resource("io.t28.auto.truth.test.type")
    object PackagePrivateClass : Resource("io.t28.auto.truth.test.type")
    object PackagePrivateClassSubject : Resource("io.t28.auto.truth.test.type")

    object AutoUserSubject : Resource("io.t28.auto.truth.test.data")
    object AutoUserTypeSubject : Resource("io.t28.auto.truth.test.data")
    object ArrayTypes : Resource("io.t28.auto.truth.test.data")
    object ArrayTypesSubject : Resource("io.t28.auto.truth.test.data")
    object BoxedPrimitiveTypes : Resource("io.t28.auto.truth.test.data")
    object BoxedPrimitiveTypesSubject : Resource("io.t28.auto.truth.test.data")
    object ClassTypes : Resource("io.t28.auto.truth.test.data")
    object ClassTypesSubject : Resource("io.t28.auto.truth.test.data")
    object EmptyPrefixUserSubject : Resource("io.t28.auto.truth.test.prefix")
    object EmptySuffixUserSubject : Resource("io.t28.auto.truth.test.suffix")
    object EnumTypes : Resource("io.t28.auto.truth.test.data")
    object EnumTypesSubject : Resource("io.t28.auto.truth.test.data")
    object GenericTypes : Resource("io.t28.auto.truth.test.data")
    object GenericTypesSubject : Resource("io.t28.auto.truth.test.data")
    object GuavaTypes : Resource("io.t28.auto.truth.test.data")
    object GuavaTypesSubject : Resource("io.t28.auto.truth.test.data")
    object InvalidPrefixUserSubject : Resource("io.t28.auto.truth.test.prefix")
    object InvalidSuffixUserSubject : Resource("io.t28.auto.truth.test.suffix")
    object IterableTypes : Resource("io.t28.auto.truth.test.data")
    object IterableTypesSubject : Resource("io.t28.auto.truth.test.data")
    object MapTypes : Resource("io.t28.auto.truth.test.data")
    object MapTypesSubject : Resource("io.t28.auto.truth.test.data")
    object OptionalTypes : Resource("io.t28.auto.truth.test.data")
    object OptionalTypesSubject : Resource("io.t28.auto.truth.test.data")
    object PathTypes : Resource("io.t28.auto.truth.test.data")
    object PathTypesSubject : Resource("io.t28.auto.truth.test.data")
    object StreamTypes : Resource("io.t28.auto.truth.test.data")
    object StreamTypesSubject : Resource("io.t28.auto.truth.test.data")
    object User : Resource("io.t28.auto.truth.test.data")
    object UserSubject : Resource("io.t28.auto.truth.test.data")
    object UserTypeSubject : Resource("io.t28.auto.truth.test.data")
    object PrimitiveTypes : Resource("io.t28.auto.truth.test.data")
    object PrimitiveTypesSubject : Resource("io.t28.auto.truth.test.data")
    object ValidPrefixUserSubject : Resource("io.t28.auto.truth.test.prefix")
    object ValidSuffixUserSubject : Resource("io.t28.auto.truth.test.suffix")

    object CustomAnnotation : Resource("io.t28.auto.truth.test") {
        override fun toJavaFileObject(): JavaFileObject {
            return forSourceString(qualifiedName, """
                package $packageName;
    
                @interface CustomAnnotation {
                    Class<?> classValue();
    
                    String stringValue();
                }
            """.trimIndent())
        }
    }

    object AnnotatedClass : Resource("io.t28.auto.truth.test") {
        override fun toJavaFileObject(): JavaFileObject {
            return forSourceString(qualifiedName, """
                package $packageName;
                    
                @CustomAnnotation(
                    classValue = String.class,
                    stringValue = "foobarbaz"
                )
                class AnnotatedClass {
                }
            """.trimIndent())
        }
    }
}
