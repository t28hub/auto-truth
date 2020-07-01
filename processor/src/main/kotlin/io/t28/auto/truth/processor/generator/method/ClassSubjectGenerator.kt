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

package io.t28.auto.truth.processor.generator.method

import com.google.common.truth.ClassSubject
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.utils.TypeUtils
import io.t28.auto.truth.processor.utils.isAssignable
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class ClassSubjectGenerator(context: Context) : TruthSubjectGenerator(context) {
    private val typeUtils: TypeUtils = context.utils

    override fun matches(type: DeclaredType): Boolean {
        return typeUtils.isAssignable<Class<*>>(type)
    }

    override fun subjectClass(type: TypeMirror): TypeName {
        return ClassName.get(ClassSubject::class.java)
    }
}
