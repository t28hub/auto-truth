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

package io.t28.auto.truth.processor.processor

import io.t28.auto.truth.AutoSubject
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.SubjectClass
import io.t28.auto.truth.processor.data.ValueObjectClass
import io.t28.auto.truth.processor.extensions.asString
import io.t28.auto.truth.processor.extensions.asType
import io.t28.auto.truth.processor.extensions.asTypeElement
import io.t28.auto.truth.processor.extensions.findAnnotationMirror
import io.t28.auto.truth.processor.extensions.getAnnotationValue
import io.t28.auto.truth.processor.extensions.getPackage
import io.t28.auto.truth.processor.extensions.isValidClassPrefix
import io.t28.auto.truth.processor.extensions.isValidClassSuffix
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

class AutoSubjectProcessor(context: Context) : Processor<TypeElement, SubjectClass> {
    private val logger = context.logger

    companion object {
        private const val VALUE_OBJECT_CLASS = "value"
        private const val SUBJECT_CLASS_PREFIX = "prefix"
        private const val SUBJECT_CLASS_SUFFIX = "suffix"
    }

    override fun process(element: TypeElement): SubjectClass {
        val annotation = element.findAnnotationMirror<AutoSubject>() ?: run {
            logger.error(element, "Required annotation %s is missing", AutoSubject::class.simpleName)
            throw IllegalStateException("Required annotation ${AutoSubject::class.simpleName} does not exist ${element.qualifiedName}")
        }

        val packageName = "${element.getPackage().qualifiedName}"
        val valueObjectType = requireNotNull(annotation.getAnnotationValue(VALUE_OBJECT_CLASS)).asType()
        val valueObjectElement = requireNotNull(valueObjectType as? DeclaredType).asTypeElement()

        val classPrefix = requireNotNull(annotation.getAnnotationValue(SUBJECT_CLASS_PREFIX)).asString()
        if (!classPrefix.isValidClassPrefix()) {
            throw ProcessingException(element, "Prefix given within @AutoTruth is invalid: %s", classPrefix)
        }

        val classSuffix = requireNotNull(annotation.getAnnotationValue(SUBJECT_CLASS_SUFFIX)).asString()
        if (!classSuffix.isValidClassSuffix()) {
            throw ProcessingException(element, "Suffix given within @AutoTruth is invalid: %s", classSuffix)
        }

        return SubjectClass(
            packageName = packageName,
            prefix = classPrefix,
            suffix = classSuffix,
            element = element,
            valueObject = ValueObjectClass(valueObjectElement)
        )
    }
}
