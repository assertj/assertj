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
import static org.assertj.core.util.introspection.ClassUtils.isPrimitiveOrWrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ClassUtils_isPrimitiveOrWrapper_Test {

  @ParameterizedTest
  @MethodSource
  void isPrimitiveOrWrapper_should_detect_primitive_types_and_their_corresponding_wrapper(Class<?> clazz) {
    // WHEN
    boolean isPrimitive = isPrimitiveOrWrapper(clazz);
    // THEN
    then(isPrimitive).isTrue();
  }

  @ParameterizedTest
  @ValueSource(classes = { Optional.class, String.class, List.class, AtomicInteger.class })
  void should_detect_as_not_primitive_types_or_their_corresponding_wrapper(Class<?> clazz) {
    // WHEN
    boolean isPrimitive = isPrimitiveOrWrapper(clazz);
    // THEN
    then(isPrimitive).isFalse();
  }

  private static Stream<Class<?>> isPrimitiveOrWrapper_should_detect_primitive_types_and_their_corresponding_wrapper() {
    return Stream.of(Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Double.class,
                     Float.class, Void.class, Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
                     Double.TYPE, Float.TYPE, Void.TYPE);
  }
}
