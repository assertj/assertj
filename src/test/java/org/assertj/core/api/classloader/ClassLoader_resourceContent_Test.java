package org.assertj.core.api.classloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockClassLoader;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockUrl;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockUrlWithContent;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockUrlWithInputStream;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.withEnumerationOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.SimpleClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassLoaderAssert#resourceContent(String)}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader resourceContent tests")
class ClassLoader_resourceContent_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    String path = "blahblah";

    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).resourceContent(path))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_path_is_null() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).resourceContent(null))
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
      .isThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz").isNull());
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz").isNotNull())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("foo/bar/baz");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_succeed_for_only_resource_match() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    byte[] firstResourceContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
    URL firstResource = mockUrlWithContent(firstResourceContent);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz")
        .isEqualTo(firstResourceContent));
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz").isNull())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("foo/bar/baz");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_close_resource_streams_afterwards() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    InputStream firstInputStream = spy(new ByteArrayInputStream(new byte[]{1, 2, 3}));
    URL firstResource = mockUrlWithInputStream(firstInputStream);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz").isNotNull());

    verify(firstInputStream).close();
  }

  @Test
  void should_close_resource_streams_if_exceptions_thrown() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    InputStream firstInputStream = mock(InputStream.class);
    URL firstResource = mockUrlWithInputStream(firstInputStream);
    when(firstInputStream.read(any(), anyInt(), anyInt()))
      .thenThrow(expectedException);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("foo/bar/baz").isNotNull())
      .isInstanceOf(UncheckedIOException.class);

    verify(firstInputStream).close();
  }

  @Test
  void should_succeed_for_multiple_resource_matches() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    byte[] firstContent = "Hello World".getBytes(StandardCharsets.UTF_8);
    URL firstResource = mockUrlWithContent(firstContent);
    URL secondResource = mockUrl();
    URL thirdResource = mockUrl();
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader)
        .resourceContent("org/example/me")
        .isEqualTo(firstContent));
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("org/example/me").isEmpty())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("org/example/me");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_propagate_IOExceptions_from_lookup_as_UncheckedIOException_instances()
    throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    when(classLoader.getResources(any()))
      .thenThrow(expectedException);

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("lorem/ipsum/dolor/sit/amet"))
      .isInstanceOf(UncheckedIOException.class)
      .hasMessage("Failed to discover resources for path 'lorem/ipsum/dolor/sit/amet'")
      .hasCause(expectedException);
  }

  @Test
  void should_propagate_IOExceptions_from_reading_as_UncheckedIOException_instances()
    throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    URL firstResource = mockUrlWithContent("Hello, World!".getBytes(StandardCharsets.UTF_8));
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));
    when(firstResource.openStream())
      .thenThrow(expectedException);

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).resourceContent("lorem/ipsum/dolor/sit/amet"))
      .isInstanceOf(UncheckedIOException.class)
      .hasMessage("Failed to read content of resource for URL " + firstResource)
      .hasCause(expectedException);
  }
}
