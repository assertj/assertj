/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.SimpleClassLoader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#parent()}.
 *
 * @author Ashley Scopes
 */
class ClassLoader_parent_Test {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).parent())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_return_assertions_for_parent_when_parent_is_null() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader)
        .parent()
        .isNull());
  }

  @Test
  void should_return_assertions_for_parent_when_parent_is_not_null() {
    // Given
    ClassLoader parentClassLoader = new SimpleClassLoader();
    ClassLoader childClassLoader = new SimpleClassLoader(parentClassLoader);

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(childClassLoader)
        .parent()
        .isNotNull()
        .isSameAs(parentClassLoader));
  }
}
