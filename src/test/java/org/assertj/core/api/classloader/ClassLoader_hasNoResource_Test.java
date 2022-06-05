package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockClassLoader;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockUrl;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.withEnumerationOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldNotHaveResource.shouldNotHaveResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.SimpleClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#hasNoResource(String)}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader hasNoResource tests")
class ClassLoader_hasNoResource_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    String path = "blahblah";

    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).hasNoResource(path))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_path_is_null() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).hasNoResource(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("the path to discover resources for should not be null");
  }


  @Test
  void should_succeed_if_no_resource_is_available_with_the_given_name() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf());

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).hasNoResource("foo/bar/baz"));

    verify(classLoader).getResources("foo/bar/baz");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_fail_for_single_resource_match() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    URL firstResource = mockUrl();
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).hasNoResource("lorem/ipsum"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotHaveResource(classLoader, "lorem/ipsum", firstResource).create());

    verify(classLoader).getResources("lorem/ipsum");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_fail_for_multiple_resource_matches() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    URL firstResource = mockUrl();
    URL secondResource = mockUrl();
    URL thirdResource = mockUrl();
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).hasNoResource("do/ray/me"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotHaveResource(classLoader, "do/ray/me", firstResource).create());

    verify(classLoader).getResources("do/ray/me");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_propagate_IOExceptions_as_UncheckedIOException_instances() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    when(classLoader.getResources(any()))
      .thenThrow(expectedException);

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).hasNoResource("lorem/ipsum/dolor/sit/amet"))
      .isInstanceOf(UncheckedIOException.class)
      .hasMessage("Failed to discover resources for path 'lorem/ipsum/dolor/sit/amet'")
      .hasCause(expectedException);
  }
}
