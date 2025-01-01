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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.Digests.digestDiff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Supplier;
import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.Diff;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.Digests;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.diff.Delta;

/**
 * Base class for all implementations of assertions for {@link InputStream}s.
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 *
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Stefan Birkner
 */
public abstract class AbstractInputStreamAssert<SELF extends AbstractInputStreamAssert<SELF, ACTUAL>, ACTUAL extends InputStream>
    extends AbstractAssert<SELF, ACTUAL> {

  private final Diff diff = new Diff();
  private final BinaryDiff binaryDiff = new BinaryDiff();

  protected AbstractInputStreamAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Converts the content of the actual {@link InputStream} to a {@link String} by decoding its bytes using the given charset
   * and returns assertions for the computed String allowing String specific assertions from this call.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @throws UncheckedIOException if an I/O error occurs.
   * @since 3.20.0
   */
  @CheckReturnValue
  public AbstractStringAssert<?> asString(Charset charset) {
    isNotNull();
    return assertThat(asString(actual, charset));
  }

  private String asString(InputStream actual, Charset charset) {
    requireNonNull(charset, shouldNotBeNull("charset")::create);
    return wrapWithMarkAndReset(actual, () -> new String(readAllBytes(actual), charset));
  }

  private static byte[] readAllBytes(InputStream is) {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] data = new byte[1024];
      for (int length; (length = is.read(data)) != -1;) {
        os.write(data, 0, length);
      }
      return os.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
   *
   * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
   * @throws UncheckedIOException if an I/O error occurs.
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
   * <b>Warning: this will consume the whole input streams in case the underlying
   * implementations do not support {@link InputStream#markSupported() marking}.</b>
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
   * @throws UncheckedIOException if an I/O error occurs.
   */
  public SELF hasSameContentAs(InputStream expected) {
    isNotNull();
    assertHasSameContentAs(expected);
    return myself;
  }

  private void assertHasSameContentAs(InputStream expected) {
    requireNonNull(expected, shouldNotBeNull("expected")::create);
    wrapWithMarkAndReset(actual, () -> wrapWithMarkAndReset(expected, () -> {
      try {
        List<Delta<String>> diffs = diff.diff(actual, expected);
        if (!diffs.isEmpty()) throw assertionError(shouldHaveSameContent(actual, expected, diffs));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }));
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is empty.
   * <p>
   * <b>Warning: this will consume the first byte of the input stream in case
   * the underlying implementation does not support {@link InputStream#markSupported() marking}.</b>
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
    wrapWithMarkAndReset(actual, () -> {
      try {
        if (actual.read() != -1) throw assertionError(shouldBeEmpty(actual));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is not empty.
   * <p>
   * <b>Warning: this will consume the first byte of the input stream in case
   * the underlying implementation does not support {@link InputStream#markSupported() marking}.</b>
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
    wrapWithMarkAndReset(actual, () -> {
      try {
        if (actual.read() == -1) throw assertionError(shouldNotBeEmpty());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the given {@code String} <b>except for newlines wich are ignored</b>.
   * <p>
   * This will change in AssertJ 4.0 where newlines will be taken into account, in the meantime, to get this behavior
   * one can use {@link #asString(Charset)} and then chain with {@link AbstractStringAssert#isEqualTo(String)}.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @throws UncheckedIOException if an I/O error occurs.
   * @since 3.11.0
   */
  public SELF hasContent(String expected) {
    isNotNull();
    assertHasContent(expected);
    return myself;
  }

  private void assertHasContent(String expected) {
    requireNonNull(expected, shouldNotBeNull("expected")::create);
    wrapWithMarkAndReset(actual, () -> {
      try {
        List<Delta<String>> diffs = diff.diff(actual, expected);
        if (!diffs.isEmpty()) throw assertionError(shouldHaveSameContent(actual, expected, diffs));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  /**
   * Verifies that the binary content of the actual {@code InputStream} is <b>exactly</b> equal to the given one.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @throws UncheckedIOException if an I/O error occurs.
   * @since 3.16.0
   */
  public SELF hasBinaryContent(byte[] expected) {
    isNotNull();
    assertHasBinaryContent(expected);
    return myself;
  }

  private void assertHasBinaryContent(byte[] expected) {
    requireNonNull(expected, shouldNotBeNull("expected")::create);
    wrapWithMarkAndReset(actual, () -> {
      try {
        BinaryDiffResult result = binaryDiff.diff(actual, expected);
        if (!result.hasNoDiff()) throw assertionError(shouldHaveBinaryContent(actual, result));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @param algorithm the MessageDigest used to calculate the digests.
   * @param expected the expected binary content to compare the actual {@code InputStream}'s digest to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest algorithm, byte[] expected) {
    isNotNull();
    assertHasDigest(algorithm, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified {@link MessageDigest}) is equal to the given one.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @param algorithm the MessageDigest used to calculate the digests.
   * @param digest the expected binary content to compare the actual {@code InputStream}'s digest to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(MessageDigest algorithm, String digest) {
    isNotNull();
    assertHasDigest(algorithm, Digests.fromHex(digest));
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, byte[] expected) {
    isNotNull();
    assertHasDigest(algorithm, expected);
    return myself;
  }

  /**
   * Verifies that the tested {@link InputStream} digest (calculated with the specified algorithm) is equal to the given one.
   * <p>
   * <b>Warning: this will consume the whole input stream in case the underlying
   * implementation does not support {@link InputStream#markSupported() marking}.</b>
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
   * @param digest the expected binary content to compare the actual {@code InputStream}'s content to.
   * @return {@code this} assertion object.
   * @throws NullPointerException  if the given algorithm is {@code null}.
   * @throws NullPointerException  if the given digest is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError        if the actual {@code InputStream} is not readable.
   * @throws UncheckedIOException if an I/O error occurs.
   * @throws AssertionError       if the content of the tested {@code InputStream}'s digest is not equal to the given one.
   * @since 3.11.0
   */
  public SELF hasDigest(String algorithm, String digest) {
    isNotNull();
    assertHasDigest(algorithm, digest);
    return myself;
  }

  private void assertHasDigest(String algorithm, String digest) {
    requireNonNull(digest, shouldNotBeNull("digest")::create);
    assertHasDigest(algorithm, Digests.fromHex(digest));
  }

  private void assertHasDigest(String algorithm, byte[] digest) {
    requireNonNull(algorithm, shouldNotBeNull("algorithm")::create);
    try {
      assertHasDigest(MessageDigest.getInstance(algorithm), digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void assertHasDigest(MessageDigest algorithm, byte[] digest) {
    requireNonNull(algorithm, shouldNotBeNull("algorithm")::create);
    requireNonNull(digest, shouldNotBeNull("digest")::create);
    wrapWithMarkAndReset(actual, () -> {
      try {
        DigestDiff diff = digestDiff(actual, algorithm, digest);
        if (diff.digestsDiffer()) throw assertionError(shouldHaveDigest(actual, diff));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  private static void wrapWithMarkAndReset(InputStream inputStream, Runnable runnable) {
    wrapWithMarkAndReset(inputStream, () -> {
      runnable.run();
      return null;
    });
  }

  private static <T> T wrapWithMarkAndReset(InputStream inputStream, Supplier<T> callable) {
    if (!inputStream.markSupported()) {
      return callable.get();
    }

    inputStream.mark(Integer.MAX_VALUE);
    try {
      return callable.get();
    } finally {
      try {
        inputStream.reset();
      } catch (IOException ignored) {}
    }
  }

}
