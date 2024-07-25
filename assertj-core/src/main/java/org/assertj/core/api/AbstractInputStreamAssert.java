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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsException;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link InputStream}s.
 *
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Stefan Birkner
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractInputStreamAssert<SELF extends AbstractInputStreamAssert<SELF, ACTUAL>, ACTUAL extends InputStream>
    extends AbstractAssert<SELF, ACTUAL> {

  @VisibleForTesting
  InputStreams inputStreams = InputStreams.instance();

  protected AbstractInputStreamAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Converts the content of the actual {@link InputStream} to a {@link String} by decoding its bytes using the given charset
   * and returns assertions for the computed String allowing String specific assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> InputStream abcInputStream = new ByteArrayInputStream("abc".getBytes());
   *
   * // assertion succeeds
   * assertThat(abcInputStream).asString(UTF_8)
   *                           .startsWith("a");
   *
   * // assertion fails
   * assertThat(abcInputStream).asString(UTF_8)
   *                           .startsWith("e");</code></pre>
   *
   * @param charset the {@link Charset} to interpret the {@code InputStream}'s content to a String
   * @return a string assertion object.
   * @throws NullPointerException if the given {@code Charset} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws InputStreamsException if an I/O error occurs.
   * @since 3.20.0
   */
  @CheckReturnValue
  public AbstractStringAssert<?> asString(Charset charset) {
    requireNonNull(charset, "The charset for converting to a String must not be null");
    objects.assertNotNull(info, actual);
    String actualAsString = readString(charset);
    return assertThat(actualAsString);
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
   *
   * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
   * @throws InputStreamsException if an I/O error occurs.
   *
   * @deprecated use {@link #hasSameContentAs(InputStream)} instead
   */
  @Deprecated
  public SELF hasContentEqualTo(InputStream expected) {
    return hasSameContentAs(expected);
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new ByteArrayInputStream(new byte[] {0xa})).hasSameContentAs(new ByteArrayInputStream(new byte[] {0xa}));
   *
   * // assertions will fail
   * assertThat(new ByteArrayInputStream(new byte[] {0xa})).hasSameContentAs(new ByteArrayInputStream(new byte[] {}));
   * assertThat(new ByteArrayInputStream(new byte[] {0xa})).hasSameContentAs(new ByteArrayInputStream(new byte[] {0xa, 0xc, 0xd}));</code></pre>
   *
   * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public SELF hasSameContentAs(InputStream expected) {
    inputStreams.assertSameContentAs(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is empty.
   * <p>
   * <b>Warning: this will consume the first byte of the {@code InputStream}.</b>
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new ByteArrayInputStream(new byte[] {})).isEmpty());
   *
   * // assertions will fail
   * assertThat(new ByteArrayInputStream(new byte[] {0xa})).isEmpty(); </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not empty.
   * @throws UncheckedIOException if an I/O error occurs.
   * @since 3.17.0
   */
  public SELF isEmpty() {
    isNotNull();
    assertIsEmpty();
    return myself;
  }

  private void assertIsEmpty() {
    try {
      if (actual.read() != -1) throw assertionError(shouldBeEmpty(actual));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is not empty.
   * <p>
   * <b>Warning: this will consume the first byte of the {@code InputStream}.</b>
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new ByteArrayInputStream(new byte[] {0xa})).isNotEmpty());
   *
   * // assertions will fail
   * assertThat(new ByteArrayInputStream(new byte[] {})).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is empty.
   * @throws UncheckedIOException if an I/O error occurs.
   * @since 3.17.0
   */
  public SELF isNotEmpty() {
    isNotNull();
    assertIsNotEmpty();
    return myself;
  }

  private void assertIsNotEmpty() {
    try {
      if (actual.read() == -1) throw assertionError(shouldNotBeEmpty());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the given {@code String} <b>except for newlines wich are ignored</b>.
   * <p>
   * This will change in AssertJ 4.0 where newlines will be taken into account, in the meantime, to get this behavior
   * one can use {@link #asString(Charset)} and then chain with {@link AbstractStringAssert#isEqualTo(String)}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new ByteArrayInputStream("a".getBytes())).hasContent("a");
   *
   * // assertions will fail
   * assertThat(new ByteArrayInputStream("a".getBytes())).hasContent("");
   * assertThat(new ByteArrayInputStream("a".getBytes())).hasContent("ab");</code></pre>
   *
   * @param expected the given {@code String} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the given {@code String}.
   * @throws InputStreamsException if an I/O error occurs.
   * @since 3.11.0
   */
  public SELF hasContent(String expected) {
    inputStreams.assertHasContent(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the binary content of the actual {@code InputStream} is <b>exactly</b> equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> InputStream inputStream = new ByteArrayInputStream(new byte[] {1, 2});
   *
   * // assertion will pass
   * assertThat(inputStream).hasContent(new byte[] {1, 2});
   *
   * // assertions will fail
   * assertThat(inputStream).hasBinaryContent(new byte[] { });
   * assertThat(inputStream).hasBinaryContent(new byte[] {0, 0});</code></pre>
   *
   * @param expected the expected binary content to compare the actual {@code InputStream}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given content is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the given binary content.
   * @throws InputStreamsException if an I/O error occurs.
   * @since 3.16.0
   */
  public SELF hasBinaryContent(byte[] expected) {
    inputStreams.assertHasBinaryContent(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * Examples:
   * <pre><code class="java"> // assume that assertj-core-2.9.0.jar was downloaded from https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar
   * InputStream tested = new FileInputStream(new File("assertj-core-2.9.0.jar"));
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), new byte[]{92, 90, -28, 91, 88, -15, 32, 35, -127, 122, -66, 73, 36, 71, -51, -57, -111, 44, 26, 44});
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), new byte[]{-36, -77, 1, 92, -46, -124, 71, 100, 76, -127, 10, -13, 82, -125, 44, 25});
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad".getBytes());
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "3735dff8e1f9df0492a34ef075205b8f".getBytes());</code></pre>
   *
   * @param digest the MessageDigest used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code InputStream}'s digest to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws InputStreamsException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest digest, byte[] expected) {
    inputStreams.assertHasDigest(info, actual, digest, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * Examples:
   * <pre><code class="java"> // assume that assertj-core-2.9.0.jar was downloaded from https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar
   * InputStream tested = new FileInputStream(new File("assertj-core-2.9.0.jar"));
   *
   * // The following assertions succeed:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "5c5ae45b58f12023817abe492447cdc7912c1a2c");
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "dcb3015cd28447644c810af352832c19");
   *
   * // The following assertions fail:
   * assertThat(tested).hasDigest(MessageDigest.getInstance("SHA1"), "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad");
   * assertThat(tested).hasDigest(MessageDigest.getInstance("MD5"), "3735dff8e1f9df0492a34ef075205b8f");</code></pre>
   *
   * @param digest the MessageDigest used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code InputStream}'s digest to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws InputStreamsException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest digest, String expected) {
    inputStreams.assertHasDigest(info, actual, digest, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * Examples:
   * <pre><code class="java"> // assume that assertj-core-2.9.0.jar was downloaded from https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar
   * InputStream tested = new FileInputStream(new File("assertj-core-2.9.0.jar"));
   *
   * // The following assertion succeeds:
   * assertThat(tested).hasDigest("SHA1", new byte[]{92, 90, -28, 91, 88, -15, 32, 35, -127, 122, -66, 73, 36, 71, -51, -57, -111, 44, 26, 44});
   * assertThat(tested).hasDigest("MD5", new byte[]{-36, -77, 1, 92, -46, -124, 71, 100, 76, -127, 10, -13, 82, -125, 44, 25});
   *
   * // The following assertion fails:
   * assertThat(tested).hasDigest("SHA1", "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad".getBytes());
   * assertThat(tested).hasDigest("MD5", "3735dff8e1f9df0492a34ef075205b8f".getBytes()); </code></pre>
   *
   * @param algorithm the algorithm used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code InputStream}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws InputStreamsException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, byte[] expected) {
    inputStreams.assertHasDigest(info, actual, algorithm, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * Examples:
   * <pre><code class="java"> // assume that assertj-core-2.9.0.jar was downloaded from https://repo1.maven.org/maven2/org/assertj/assertj-core/2.9.0/assertj-core-2.9.0.jar
   * InputStream tested = new FileInputStream(new File("assertj-core-2.9.0.jar"));
   *
   * // The following assertion succeeds:
   * assertThat(tested).hasDigest("SHA1", "5c5ae45b58f12023817abe492447cdc7912c1a2c");
   * assertThat(tested).hasDigest("MD5", "dcb3015cd28447644c810af352832c19");
   *
   * // The following assertion fails:
   * assertThat(tested).hasDigest("SHA1", "93b9ced2ee5b3f0f4c8e640e77470dab031d4cad");
   * assertThat(tested).hasDigest("MD5", "3735dff8e1f9df0492a34ef075205b8f"); </code></pre>
   *
   * @param algorithm the algorithm used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code InputStream}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws InputStreamsException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, String expected) {
    inputStreams.assertHasDigest(info, actual, algorithm, expected);
    return myself;
  }

  private String readString(Charset charset) {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int read;
      byte[] data = new byte[1024];
      while ((read = actual.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, read);
      }
      buffer.flush();
      return new String(buffer.toByteArray(), charset);
    } catch (IOException e) {
      throw new InputStreamsException("Unable to read contents of InputStreams actual", e);
    }
  }
}
