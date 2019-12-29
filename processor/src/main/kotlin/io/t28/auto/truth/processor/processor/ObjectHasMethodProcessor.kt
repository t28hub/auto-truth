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

import com.squareup.javapoet.MethodSpec
import io.t28.auto.truth.processor.dsl.method
import io.t28.auto.truth.processor.dsl.param
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeMirror

class ObjectHasMethodProcessor(
    private val type: TypeMirror,
    private val name: String,
    private val symbol: String
) : Processor<MethodSpec> {
    override fun process(): MethodSpec {
        return method("has${name.capitalize()}", param(type, "expected")) {
            modifiers(Modifier.PUBLIC)
            statement("check(\$S).that(this.\$L.\$L).isEqualTo(\$L)", symbol, "actual", symbol, "expected")
        }
    }
}
