/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.introspection.ClassUtils.haveSameClassNameInDifferentPackages;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClassUtils_haveSameClassNameInDifferentPackages_Test {

  @ParameterizedTest
  @MethodSource
  void should_return_true(Object object1, Object object2) {
    assertThat(haveSameClassNameInDifferentPackages(object1, object2)).isTrue();
  }

  static Stream<Arguments> should_return_true() {
    return Stream.of(arguments(new Date(123), new java.sql.Date(123)),
                     arguments(new org.assertj.core.internal.objects.pkg1.Foo("foo"),
                               new org.assertj.core.internal.objects.pkg2.Foo("foo")));
  }

  @ParameterizedTest
  @MethodSource
  void should_return_false(Object object1, Object object2) {
    assertThat(haveSameClassNameInDifferentPackages(object1, object2)).isFalse();
  }

  static Stream<Arguments> should_return_false() {
    return Stream.of(arguments(new Date(123), new Date(123)),
                     arguments(new Date(123), "foo"),
                     arguments(null, "foo"),
                     arguments("foo", null),
                     arguments(null, null));
  }
}
