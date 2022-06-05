package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldNotBeSystemClassLoader.shouldNotBeSystemClassLoader;

import org.assertj.core.api.AbstractClassLoaderAssert;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.SimpleClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#isNotSystemClassLoader()}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader isNotSystemClassLoader tests")
class ClassLoader_isNotSystemClassLoader_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).isNotSystemClassLoader())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_system_class_loader() {
    // Given
    ClassLoader classLoader = ClassLoaderTestUtils.systemClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).isNotSystemClassLoader())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeSystemClassLoader(classLoader).create());
  }

  @Test
  void should_succeed_if_actual_is_system_class_loader() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).isNotSystemClassLoader());
  }

  @Test
  void should_return_class_loader_assert_when_successful() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();
    AbstractClassLoaderAssert<?> expectedAssertions = assertThat(classLoader);

    // When
    AbstractClassLoaderAssert<?> actualAssertions = expectedAssertions.isNotSystemClassLoader();

    // Then
    assertThat(actualAssertions)
      .isSameAs(expectedAssertions);
  }
}
