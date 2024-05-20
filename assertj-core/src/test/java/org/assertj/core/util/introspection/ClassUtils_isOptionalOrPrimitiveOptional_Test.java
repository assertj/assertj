/*
 * Copyright © 2024 the original author or authors.
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
package org.assertj.core.util.introspection;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.introspection.ClassUtils.isOptionalOrPrimitiveOptional;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClassUtils_isOptionalOrPrimitiveOptional_Test {

  @ParameterizedTest
  @ValueSource(classes = { Optional.class, OptionalLong.class, OptionalDouble.class, OptionalInt.class })
  void should_detect_Optional_and_primitive_Optional_types(Class<?> clazz) {
    // WHEN
    boolean isPrimitive = isOptionalOrPrimitiveOptional(clazz);
    // THEN
    then(isPrimitive).isTrue();
  }

  @ParameterizedTest
  @ValueSource(classes = { String.class, com.google.common.base.Optional.class })
  void should_detect_as_not_from_the_Optional_family(Class<?> clazz) {
    // WHEN
    boolean isPrimitive = isOptionalOrPrimitiveOptional(clazz);
    // THEN
    then(isPrimitive).isFalse();
  }
}
