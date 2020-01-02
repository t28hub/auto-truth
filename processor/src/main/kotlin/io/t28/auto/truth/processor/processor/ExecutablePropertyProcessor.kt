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

import io.t28.auto.truth.processor.Context
import io.t28.auto.truth.processor.data.Property
import javax.lang.model.element.ExecutableElement

class ExecutablePropertyProcessor(context: Context) : PropertyProcessor<ExecutableElement>(context) {
    override fun process(element: ExecutableElement): Property {
        context.logger.debug(element, "Processing executable property: %s", element.simpleName)

        val simpleName = "${element.simpleName}"
        return Property(
            element = element,
            type = element.returnType,
            name = simpleName.simplify(),
            symbol = "$simpleName()"
        )
    }
}