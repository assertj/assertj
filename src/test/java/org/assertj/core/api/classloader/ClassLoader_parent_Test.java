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
