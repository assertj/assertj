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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ContentsShouldBeEqualTo.contentsShouldBeEqualTo;
import static org.assertj.core.error.ContentsShouldContain.contentsShouldContain;
import static org.assertj.core.error.ContentsShouldEndWith.contentsShouldEndWith;
import static org.assertj.core.error.ContentsShouldStartWith.contentsShouldStartWith;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;
import static org.assertj.core.error.ShouldBeMarked.shouldNotBeMarked;
import static org.assertj.core.error.ShouldBeMarkedAt.shouldBeMarkedAt;

import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;
import java.nio.charset.Charset;

import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.Comparables;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link ByteBuffer}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Jean de Leeuw
 */
public class AbstractByteBufferAssert<SELF extends AbstractByteBufferAssert<SELF>>
    extends AbstractBufferAssert<SELF, ByteBuffer> {

  /**
   * Used to compare a ByteBuffer with another ByteBuffer.
   */
  @VisibleForTesting
  Comparables comparables = new Comparables();

  /**
   * Used to compare the content of the ByteBuffer with byte arrays.
   */
  @VisibleForTesting
  ByteArrays byteArrays = ByteArrays.instance();

  public AbstractByteBufferAssert(ByteBuffer buffer, Class<?> selfType) {
    super(buffer, selfType);
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} equals the given expected string using the platform's
   * default charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).isEqualTo("test");
   *
   * // ... but this one fails as the content of "buffer" does not equal the given expected string.
   * assertThat(buffer).isEqualTo("test two");
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not equal the given expected string.
   */
  public SELF isEqualTo(String expected) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual));
    if (!contentString.equals(expected)) throwAssertionError(contentsShouldBeEqualTo(expected, actual, Charset.defaultCharset()));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} equals the given expected string using the
   * specified charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = StandardCharsets.UTF_8.encode("test");
   *
   * // this assertion succeeds ...
   * assertThat(buffer).isEqualTo("test", StandardCharsets.UTF_8);
   *
   * // ... but this one fails as the content of "buffer" does not equal the given expected string.
   * assertThat(buffer).isEqualTo("test two", StandardCharsets.UTF_8);
   *
   * // ... this one also fails but because the charsets do not match.
   * assertThat(buffer).isEqualTo("test", StandardCharsets.UTF_16)
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not equal the given expected string.
   */
  public SELF isEqualTo(String expected, Charset charset) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual), charset);
    if (!contentString.equals(expected)) throwAssertionError(contentsShouldBeEqualTo(expected, actual, charset));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} equals the given expected byte array.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).isEqualTo("test".getBytes());
   *
   * // ... but this one fails as the content of "buffer" does not equal the given expected byte array.
   * assertThat(buffer).isEqualTo("test two".getBytes());
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not equal the given expected byte array.
   */
  public SELF isEqualTo(byte[] expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertContainsExactly(info, getContent(actual), expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} equals the given expected {@code ByteBuffer}.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * ByteBuffer expected = ByteBuffer.wrap("test".getBytes());
   * assertThat(actual).isEqualTo(expected);
   *
   * // ... but this one fails as the content of "buffer" does not equal the given expected {@code ByteBuffer}.
   * ByteBuffer expected = ByteBuffer.wrap("test two".getBytes());
   * assertThat(actual).isEqualTo(expected);
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not equal the given expected {@code ByeBuffer}.
   */
  public SELF isEqualTo(ByteBuffer expected) {
    isNotNull();
    isFlipped();

    comparables.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} contains the given expected string using the platform's
   * default charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).contains("es");
   *
   * // ... but this one fails as the content of "buffer" does not contain the given expected string.
   * assertThat(buffer).contains("xy");
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not contain the given expected string.
   */
  public SELF contains(String expected) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual));
    if (!contentString.contains(expected)) throwAssertionError(contentsShouldContain(expected, actual, Charset.defaultCharset()));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} contains the given expected string using the
   * specified charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = StandardCharsets.UTF_8.encode("test");
   *
   * // this assertion succeeds ...
   * assertThat(buffer).contains("es", StandardCharsets.UTF_8);
   *
   * // ... but this one fails as the content of "buffer" does not contain the given expected string.
   * assertThat(buffer).contains("xy", StandardCharsets.UTF_8);
   *
   * // ... this one also fails but because the charsets do not match.
   * assertThat(buffer).contains("es", StandardCharsets.UTF_16)
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not contain the given expected string.
   */
  public SELF contains(String expected, Charset charset) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual), charset);
    if (!contentString.contains(expected)) throwAssertionError(contentsShouldContain(expected, actual, charset));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} contains the given expected byte array.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).contains("test".getBytes());
   *
   * // ... but this one fails as the content of "buffer" does not contain the given expected byte array.
   * assertThat(buffer).contains("test two".getBytes());
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not contain the given expected byte array.
   */
  public SELF contains(byte[] expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertContains(info, getContent(actual), expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} contains the given expected {@code ByteBuffer}.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * ByteBuffer expected = ByteBuffer.wrap("test".getBytes());
   * assertThat(actual).contains(expected);
   *
   * // ... but this one fails as the content of "buffer" does not contain the given expected {@code ByteBuffer}.
   * ByteBuffer expected = ByteBuffer.wrap("test two".getBytes());
   * assertThat(actual).contains(expected);
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not contain the given expected {@code ByeBuffer}.
   */
  public SELF contains(ByteBuffer expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertContains(info, getContent(actual), getContent(expected));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} starts with the given expected string using the platform's
   * default charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).startsWith("te");
   *
   * // ... but this one fails as the content of "buffer" does not start with the given expected string.
   * assertThat(buffer).startsWith("es");
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not start with the given expected string.
   */
  public SELF startsWith(String expected) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual));
    if (!contentString.startsWith(expected))
      throwAssertionError(contentsShouldStartWith(expected, actual, Charset.defaultCharset()));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} starts with the given expected string using the
   * specified charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = StandardCharsets.UTF_8.encode("test");
   *
   * // this assertion succeeds ...
   * assertThat(buffer).startsWith("test", StandardCharsets.UTF_8);
   *
   * // ... but this one fails as the content of "buffer" does not start with the given expected string.
   * assertThat(buffer).startsWith("test two", StandardCharsets.UTF_8);
   *
   * // ... this one also fails but because the charsets do not match.
   * assertThat(buffer).startsWith("test", StandardCharsets.UTF_16)
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not start with the given expected string.
   */
  public SELF startsWith(String expected, Charset charset) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual), charset);
    if (!contentString.startsWith(expected)) throwAssertionError(contentsShouldStartWith(expected, actual, charset));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} starts with the given expected byte array.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).startsWith("test".getBytes());
   *
   * // ... but this one fails as the content of "buffer" does not start with the given expected byte array.
   * assertThat(buffer).startsWith("test two".getBytes());
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not start with the given expected byte array.
   */
  public SELF startsWith(byte[] expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertStartsWith(info, getContent(actual), expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} starts with the given expected {@code ByteBuffer}.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * ByteBuffer expected = ByteBuffer.wrap("test".getBytes());
   * assertThat(actual).startsWith(expected);
   *
   * // ... but this one fails as the content of "buffer" does not start with the given expected {@code ByteBuffer}.
   * ByteBuffer expected = ByteBuffer.wrap("test two".getBytes());
   * assertThat(actual).startsWith(expected);
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not start with the given expected {@code ByeBuffer}.
   */
  public SELF startsWith(ByteBuffer expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertStartsWith(info, getContent(actual), getContent(expected));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} ends with the given expected string using the platform's
   * default charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).endsWith("st");
   *
   * // ... but this one fails as the content of "buffer" does not end with the given expected string.
   * assertThat(buffer).endsWith("es");
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not end with the given expected string.
   */
  public SELF endsWith(String expected) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual));
    if (!contentString.endsWith(expected)) throwAssertionError(contentsShouldEndWith(expected, actual, Charset.defaultCharset()));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} ends with the given expected string using the
   * specified charset.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = StandardCharsets.UTF_8.encode("test");
   *
   * // this assertion succeeds ...
   * assertThat(buffer).endsWith("test", StandardCharsets.UTF_8);
   *
   * // ... but this one fails as the content of "buffer" does not end with the given expected string.
   * assertThat(buffer).endsWith("test two", StandardCharsets.UTF_8);
   *
   * // ... this one also fails but because the charsets do not match.
   * assertThat(buffer).endsWith("test", StandardCharsets.UTF_16)
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not end with the given expected string.
   */
  public SELF endsWith(String expected, Charset charset) {
    isNotNull();
    isFlipped();

    String contentString = new String(getContent(actual), charset);
    if (!contentString.endsWith(expected)) throwAssertionError(contentsShouldEndWith(expected, actual, charset));
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} ends with the given expected byte array.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(buffer).endsWith("test".getBytes());
   *
   * // ... but this one fails as the content of "buffer" does not end with the given expected byte array.
   * assertThat(buffer).endsWith("test two".getBytes());
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not end with the given expected byte array.
   */
  public SELF endsWith(byte[] expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertEndsWith(info, getContent(actual), expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code ByteBuffer} ends with the given expected {@code ByteBuffer}.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * ByteBuffer expected = ByteBuffer.wrap("test".getBytes());
   * assertThat(actual).endsWith(expected);
   *
   * // ... but this one fails as the content of "buffer" does not end with the given expected {@code ByteBuffer}.
   * ByteBuffer expected = ByteBuffer.wrap("test two".getBytes());
   * assertThat(actual).endsWith(expected);
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} has not been flipped.
   * @throws AssertionError if the actual {@code ByteBuffer}s content does not end with the given expected {@code ByeBuffer}.
   */
  public SELF endsWith(ByteBuffer expected) {
    isNotNull();
    isFlipped();

    byteArrays.assertEndsWith(info, getContent(actual), getContent(expected));
    return myself;
  }

  /**
   * Verifies that the actual {@code ByteBuffer} is marked.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * actual.mark();
   * assertThat(actual).isMarked();
   *
   * // ... but this one fails as "buffer" was never marked.
   * assertThat(actual).isMarked();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} is not marked.
   */
  public SELF isMarked() {
    isNotNull();
    try {
      actual.duplicate().reset();
    } catch (InvalidMarkException e) {
      throwAssertionError(shouldBeMarked(actual));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code ByteBuffer} is marked at the given expected position.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   * actual.mark();
   *
   * // this assertion succeeds ...
   * assertThat(actual).isMarkedAt(0);
   *
   * // ... but this one fails as "buffer" was marked at position 0.
   * assertThat(actual).isMarkedAt(1);
   * </code></pre>
   *
   * @param expected integer value representing the expected marked position of the buffer.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} is not marked.
   * @throws AssertionError if the actual {@code ByteBuffer} is marked at a different position than the given expected position.
   */
  public SELF isMarkedAt(int expected) {
    isNotNull();
    try {
      int markedPosition = actual.duplicate().reset().position();
      if (markedPosition != expected) throwAssertionError(shouldBeMarkedAt(expected, markedPosition, actual));
    } catch (InvalidMarkException e) {
      throwAssertionError(shouldBeMarked(actual));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code ByteBuffer} is not marked.
   *
   * Example:
   *
   * <pre><code class='java'>
   * ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
   *
   * // this assertion succeeds ...
   * assertThat(actual).isNotMarked();
   *
   * // ... but this one fails as "buffer" was marked.
   * actual.mark();
   * assertThat(actual).isMarked();
   * </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ByteBuffer} is {@code null}.
   * @throws AssertionError if the actual {@code ByteBuffer} is marked.
   */
  public SELF isNotMarked() {
    isNotNull();
    try {
      actual.duplicate().reset();
      throwAssertionError(shouldNotBeMarked(actual));
    } catch (InvalidMarkException ignored) {}
    return myself;
  }

  private byte[] getContent(ByteBuffer buffer) {
    byte[] content = new byte[buffer.limit()];
    for (int i = 0; i < content.length; i++) {
      content[i] = buffer.get();
    }
    buffer.rewind();
    return content;
  }
}
