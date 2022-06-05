package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldHaveParent.shouldHaveParent;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.SimpleClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#hasParent()}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader hasParent tests")
class ClassLoader_hasParent_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).hasParent())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_has_no_parent() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).hasParent())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveParent(classLoader).create());
  }

  @Test
  void should_succeed_if_actual_has_parent() {
    // Given
    ClassLoader parentClassLoader = new SimpleClassLoader();
    ClassLoader childClassLoader = new SimpleClassLoader(parentClassLoader);

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(childClassLoader).hasParent());
  }

  @Test
  void should_return_class_loader_assert_when_successful() {
    // Given
    ClassLoader parentClassLoader = new SimpleClassLoader();
    ClassLoader childClassLoader = new SimpleClassLoader(parentClassLoader);
    AbstractClassLoaderAssert<?> expectedAssertions = assertThat(childClassLoader);

    // When
    AbstractClassLoaderAssert<?> actualAssertions = expectedAssertions.hasParent();

    // Then
    assertThat(actualAssertions)
      .isSameAs(expectedAssertions);
  }
}
