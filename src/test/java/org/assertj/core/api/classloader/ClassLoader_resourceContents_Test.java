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
import static org.mockito.Mockito.verifyNoInteractions;
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
 * Tests for {@link ClassLoaderAssert#resourceContents(String)}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader resourceContents tests")
@SuppressWarnings("ResultOfMethodCallIgnored")
class ClassLoader_resourceContents_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    String path = "blahblah";

    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).resourceContents(path))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_path_is_null() {
    // Given
    ClassLoader classLoader = new SimpleClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).resourceContents(null))
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
      .isThrownBy(() -> assertThat(classLoader).resourceContents("foo/bar/baz").isEmpty());
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("foo/bar/baz").isNull())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("foo/bar/baz");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_succeed_for_only_resource_match() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    byte[] firstresourceContents = "Hello, World!".getBytes(StandardCharsets.UTF_8);
    URL firstResource = mockUrlWithContent(firstresourceContents);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader)
        .resourceContents("foo/bar/baz")
        .singleElement()
        .isEqualTo(firstresourceContents));
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("foo/bar/baz").isNull())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("foo/bar/baz");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_succeed_for_multiple_resource_matches() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    byte[] firstContent = "Hello World".getBytes(StandardCharsets.UTF_8);
    byte[] secondContent = "Goodbye World".getBytes(StandardCharsets.UTF_8);
    byte[] thirdContent = "I ran out of test data".getBytes(StandardCharsets.UTF_8);
    URL firstResource = mockUrlWithContent(firstContent);
    URL secondResource = mockUrlWithContent(secondContent);
    URL thirdResource = mockUrlWithContent(thirdContent);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader)
        .resourceContents("org/example/me")
        .hasSize(3)
        .containsExactly(firstContent, secondContent, thirdContent));
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("org/example/me").isEmpty())
      .isInstanceOf(AssertionError.class);

    verify(classLoader, times(2)).getResources("org/example/me");
    verifyNoMoreInteractions(classLoader);
  }

  @Test
  void should_close_resource_streams_afterwards() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    InputStream firstInputStream = spy(new ByteArrayInputStream(new byte[]{1, 2, 3}));
    InputStream secondInputStream = spy(new ByteArrayInputStream(new byte[]{4, 5, 6}));
    InputStream thirdInputStream = spy(new ByteArrayInputStream(new byte[]{7, 8, 9}));
    URL firstResource = mockUrlWithInputStream(firstInputStream);
    URL secondResource = mockUrlWithInputStream(secondInputStream);
    URL thirdResource = mockUrlWithInputStream(thirdInputStream);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader).resourceContents("foo/bar/baz").isNotNull());

    verify(firstInputStream).close();
    verify(secondInputStream).close();
    verify(thirdInputStream).close();
  }

  @Test
  void should_close_resource_streams_if_exceptions_thrown() throws IOException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    InputStream firstInputStream = spy(new ByteArrayInputStream(new byte[]{1, 2, 3}));
    InputStream secondInputStream = mock(InputStream.class);
    InputStream thirdInputStream = spy(new ByteArrayInputStream(new byte[]{7, 8, 9}));
    URL firstResource = mockUrlWithInputStream(firstInputStream);
    URL secondResource = mockUrlWithInputStream(secondInputStream);
    URL thirdResource = mockUrlWithInputStream(thirdInputStream);
    when(secondInputStream.read(any(), anyInt(), anyInt()))
      .thenThrow(expectedException);
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("foo/bar/baz").isNotNull())
      .isInstanceOf(UncheckedIOException.class);

    verify(firstInputStream).close();
    verify(secondInputStream).close();
    // Should never be opened, since we don't get this far.
    verifyNoInteractions(thirdResource);
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
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("lorem/ipsum/dolor/sit/amet"))
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
    URL secondResource = mockUrlWithContent("Goodbye, World!".getBytes(StandardCharsets.UTF_8));
    URL thirdResource = mockUrl();
    IOException expectedException = new IOException("Server caught fire, everyone panic");
    when(classLoader.getResources(any()))
      .thenAnswer(withEnumerationOf(firstResource, secondResource, thirdResource));
    when(thirdResource.openStream())
      .thenThrow(expectedException);

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).resourceContents("lorem/ipsum/dolor/sit/amet"))
      .isInstanceOf(UncheckedIOException.class)
      .hasMessage("Failed to read content of resource for URL " + thirdResource)
      .hasCause(expectedException);

    verify(firstResource).openStream();
    verify(secondResource).openStream();
    verify(thirdResource).openStream();
  }
}
