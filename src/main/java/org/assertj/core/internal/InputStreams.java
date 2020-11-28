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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.internal.Digests.digestDiff;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;

/**
 * Reusable assertions for <code>{@link InputStream}</code>s.
 *
 * @author Matthieu Baechler
 */
public class InputStreams {

  private static final InputStreams INSTANCE = new InputStreams();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static InputStreams instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Diff diff = new Diff();
  @VisibleForTesting
  BinaryDiff binaryDiff = new BinaryDiff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  InputStreams() {}

  /**
   * Asserts that the given InputStreams have same content.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" InputStream.
   * @param expected the "expected" InputStream.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the given InputStreams do not have same content.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertSameContentAs(AssertionInfo info, InputStream actual, InputStream expected) {
    requireNonNull(expected, "The InputStream to compare to should not be null");
    assertNotNull(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      String msg = format("Unable to compare contents of InputStreams:%n  <%s>%nand:%n  <%s>", actual, expected);
      throw new InputStreamsException(msg, e);
    }
  }

  /**
   * Asserts that the given InputStreams is empty.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" InputStream.
   * @throws AssertionError if {@code actual} is not empty.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertIsEmpty(AssertionInfo info, InputStream actual) {
    assertNotNull(info, actual);
    try {
      if (actual.read() == -1) return;
      throw failures.failure(info, shouldBeEmpty(actual));
    } catch (IOException e) {
      throw new InputStreamsException("Unable to read contents of InputStreams actual", e);
    }
  }

  /**
   * Asserts that the given InputStreams is not empty.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" InputStream.
   * @throws AssertionError if {@code actual} is not empty.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertIsNotEmpty(AssertionInfo info, InputStream actual) {
    assertNotNull(info, actual);
    try {
      if (actual.read() == -1) throw failures.failure(info, shouldNotBeEmpty());
    } catch (IOException e) {
      throw new InputStreamsException("Unable to read contents of InputStreams actual", e);
    }
  }

  /**
   * Asserts that the given InputStream has the same content as the given String.
   *
   * @param info contains information about the assertion.
   * @param actual the actual InputStream.
   * @param expected the expected String.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the given InputStream does not have the same content as the given String.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertHasContent(AssertionInfo info, InputStream actual, String expected) {
    requireNonNull(expected, "The String to compare to should not be null");
    assertNotNull(info, actual);
    try {
      List<Delta<String>> diffs = diff.diff(actual, expected);
      if (diffs.isEmpty()) return;
      throw failures.failure(info, shouldHaveSameContent(actual, expected, diffs));
    } catch (IOException e) {
      String msg = format("Unable to compare contents of InputStream:%n  <%s>%nand String:%n  <%s>", actual, expected);
      throw new InputStreamsException(msg, e);
    }
  }

  /**
   * Asserts that the given InputStream has the given binary content.
   * @param info contains information about the assertion.
   * @param actual the actual InputStream.
   * @param expected the expected binary content.
   * @throws NullPointerException if {@code expected} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the given InputStream does not have the same content as the given String.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public void assertHasBinaryContent(AssertionInfo info, InputStream actual, byte[] expected) {
    requireNonNull(expected, "The binary content to compare to should not be null");
    assertNotNull(info, actual);
    try {
      BinaryDiffResult result = binaryDiff.diff(actual, expected);
      if (result.hasNoDiff()) return;
      throw failures.failure(info, shouldHaveBinaryContent(actual, result));
    } catch (IOException e) {
      throw new InputStreamsException(format("Unable to verify binary contents of InputStream:%n  <%s>", actual), e);
    }
  }

  private static void assertNotNull(AssertionInfo info, InputStream stream) {
    Objects.instance().assertNotNull(info, stream);
  }

  public void assertHasDigest(AssertionInfo info, InputStream actual, MessageDigest digest, byte[] expected) {
    requireNonNull(digest, "The message digest algorithm should not be null");
    requireNonNull(expected, "The binary representation of digest to compare to should not be null");
    assertNotNull(info, actual);
    try {
      DigestDiff diff = digestDiff(actual, digest, expected);
      if (diff.digestsDiffer()) throw failures.failure(info, shouldHaveDigest(actual, diff));
    } catch (IOException e) {
      String msg = format("Unable to calculate digest of InputStream:%n  <%s>", actual);
      throw new InputStreamsException(msg, e);
    }
  }

  public void assertHasDigest(AssertionInfo info, InputStream actual, MessageDigest digest, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, digest, Digests.fromHex(expected));
  }

  public void assertHasDigest(AssertionInfo info, InputStream actual, String algorithm, byte[] expected) {
    requireNonNull(algorithm, "The message digest algorithm should not be null");
    try {
      assertHasDigest(info, actual, MessageDigest.getInstance(algorithm), expected);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(format("Unable to find digest implementation for: <%s>", algorithm), e);
    }
  }

  public void assertHasDigest(AssertionInfo info, InputStream actual, String algorithm, String expected) {
    requireNonNull(expected, "The string representation of digest to compare to should not be null");
    assertHasDigest(info, actual, algorithm, Digests.fromHex(expected));
  }
}
