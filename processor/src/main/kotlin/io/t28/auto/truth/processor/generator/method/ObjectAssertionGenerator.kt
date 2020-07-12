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

package io.t28.auto.truth.processor.generator.method

import com.google.common.base.Preconditions
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import io.t28.auto.truth.processor.extensions.isNull
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ErrorType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeVariable

class ObjectAssertionGenerator(private val context: Context) : MethodGenerator {
    override fun isSupported(property: Property): Boolean {
        if (property is Property.EnumConstant) {
            return false
        }
        if (property is Property.Getter && property.element.typeParameters.isNotEmpty()) {
            return false
        }
        return SupportedObjectTypeMatcher.visit(property.type, context)
    }

    override fun generate(input: Property): MethodSpec {
        require(isSupported(input))
        context.logger.debug(input.element, "Generating an assertion method for %s", input.type)

        return MethodSpec.methodBuilder("has${input.name.capitalize()}").apply {
            addModifiers(PUBLIC)
            addParameter(ParameterSpec.builder(TypeName.get(input.type), "expected").build())
            addStatement("final \$T actual = \$T.checkNotNull(this.actual).\$L", input.type, Preconditions::class.java, input.symbol)
            addStatement("check(\$S).that(actual).isEqualTo(\$L)", input.symbol, "expected")
        }.build()
    }

    internal object SupportedObjectTypeMatcher : SupportedTypeMatcher<Context>() {
        // Following classes are handled by other generators
        private val IGNORED_CLASSES = arrayOf(
            java.lang.Void::class,
            java.lang.Class::class,
            java.lang.Iterable::class,
            java.util.Map::class,
            java.util.Optional::class,
            java.util.OptionalInt::class,
            java.util.OptionalLong::class,
            java.util.OptionalDouble::class,
            java.nio.file.Path::class,
            java.util.stream.Stream::class,
            java.util.stream.IntStream::class,
            java.util.stream.LongStream::class,
            com.google.common.base.Optional::class,
            com.google.common.collect.Multimap::class,
            com.google.common.collect.Multiset::class,
            com.google.common.collect.Table::class
        )

        override fun visitPrimitive(type: PrimitiveType, context: Context): Boolean {
            return type.kind != BOOLEAN
        }

        override fun visitDeclared(type: DeclaredType, context: Context): Boolean {
            val utils = context.utils
            return IGNORED_CLASSES
                .mapNotNull { ignoredClass -> utils.getDeclaredType(ignoredClass) }
                .all { ignoredType -> !utils.isAssignableType(type, ignoredType) }
        }

        override fun visitError(type: ErrorType, context: Context): Boolean {
            return visitDeclared(type, context)
        }

        override fun visitTypeVariable(type: TypeVariable, context: Context): Boolean {
            if (type.lowerBound.isNull) {
                return true
            }
            return SupportedObjectTypeMatcher.visit(type.lowerBound, context)
        }
    }
}
