package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldNotBePrivate.shouldNotBePrivate;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.NonPrivateClassLoader;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.PrivateClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#isNotPrivate()}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader isNotPrivate tests")
class ClassLoader_isNotPrivate_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).isNotPrivate())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_private() {
    // Given
    ClassLoader classLoader = new PrivateClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).isNotPrivate())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBePrivate(classLoader).create());
  }

  @Test
  void should_succeed_if_actual_is_private() {
    // Given
    ClassLoader classLoader = new NonPrivateClassLoader();

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).isNotPrivate());
  }

  @Test
  void should_return_class_loader_assert_when_successful() {
    // Given
    ClassLoader classLoader = new NonPrivateClassLoader();
    AbstractClassLoaderAssert<?> expectedAssertions = assertThat(classLoader);

    // When
    AbstractClassLoaderAssert<?> actualAssertions = expectedAssertions.isNotPrivate();

    // Then
    assertThat(actualAssertions)
      .isSameAs(expectedAssertions);
  }
}
