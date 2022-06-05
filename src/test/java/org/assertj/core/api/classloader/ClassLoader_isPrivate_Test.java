package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldBePrivate.shouldBePrivate;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.NonPrivateClassLoader;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.PrivateClassLoader;
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
    ClassLoader classLoader = new NonPrivateClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).isPrivate())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBePrivate(classLoader).create());
  }

  @Test
  void should_succeed_if_actual_is_private() {
    // Given
    ClassLoader classLoader = new PrivateClassLoader();

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).isPrivate());
  }

  @Test
  void should_return_class_loader_assert_when_successful() {
    // Given
    ClassLoader classLoader = new PrivateClassLoader();
    AbstractClassLoaderAssert<?> expectedAssertions = assertThat(classLoader);

    // When
    AbstractClassLoaderAssert<?> actualAssertions = expectedAssertions.isPrivate();

    // Then
    assertThat(actualAssertions)
      .isSameAs(expectedAssertions);
  }
}
