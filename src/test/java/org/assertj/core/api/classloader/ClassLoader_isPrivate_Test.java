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
import static org.assertj.core.error.classloader.ShouldBePrivate.shouldBePrivate;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.NonPrivateJmxClassLoader;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.PrivateJmxClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#isPrivate()}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader isPrivate tests")
class ClassLoader_isPrivate_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).isPrivate())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_not_private() {
    // Given
    ClassLoader classLoader = new NonPrivateJmxClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).isPrivate())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBePrivate(classLoader).create());
  }

  @Test
  void should_succeed_if_actual_is_private() {
    // Given
    ClassLoader classLoader = new PrivateJmxClassLoader();

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).isPrivate());
  }

  @Test
  void should_return_class_loader_assert_when_successful() {
    // Given
    ClassLoader classLoader = new PrivateJmxClassLoader();
    AbstractClassLoaderAssert<?> expectedAssertions = assertThat(classLoader);

    // When
    AbstractClassLoaderAssert<?> actualAssertions = expectedAssertions.isPrivate();

    // Then
    assertThat(actualAssertions)
      .isSameAs(expectedAssertions);
  }
}
