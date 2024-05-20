/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.files;

import static java.nio.file.Files.readAllBytes;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.internal.Digests.toHex;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.Digests;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Tests for <code>{@link Files#assertHasDigest(AssertionInfo, File, MessageDigest, String)}</code>
 *
 * @author Valeriy Vyrva
 */
class Files_assertHasDigest_AlgorithmString_Test extends FilesBaseTest {
  private final String algorithm = "MD5";
  private final String expected = "";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_with_should_exist_error_if_actual_does_not_exist() {
    // GIVEN
    File actual = new File("xyz");
    // WHEN
    expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldExist(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_file() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/tmp");
    // WHEN
    expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(actual));
  }

  @DisabledOnOs(OS.WINDOWS)
  @Test
  void should_fail_if_actual_exists_but_is_not_readable() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/Test.java");
    actual.setReadable(false);
    // WHEN
    expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeReadable(actual));
  }

  @Test
  void should_throw_error_if_digest_is_null() {
    // GIVEN
    MessageDigest digest = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> underTest.assertHasDigest(INFO, actual, digest, expected),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The message digest algorithm should not be null");
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    // GIVEN
    byte[] expected = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The binary representation of digest to compare to should not be null");
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    IOException cause = new IOException();
    given(nioFilesWrapper.newInputStream(any())).willThrow(cause);
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  void should_throw_error_wrapping_caught_NoSuchAlgorithmException() {
    // GIVEN
    String unknownDigestAlgorithm = "UnknownDigestAlgorithm";
    // WHEN
    IllegalStateException ise = catchThrowableOfType(() -> underTest.assertHasDigest(INFO, actual, unknownDigestAlgorithm,
                                                                                     expected),
                                                     IllegalStateException.class);
    // THEN
    then(ise).hasMessage("Unable to find digest implementation for: <UnknownDigestAlgorithm>");
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_digest() throws IOException, NoSuchAlgorithmException {
    // GIVEN
    String algorithm = "MD5";
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    writeByteArrayToFile(actual, "Bad Content".getBytes());
    MessageDigest digest = MessageDigest.getInstance("MD5");
    String expected = toHex(digest.digest("Content".getBytes()));
    DigestDiff digestDiff = new DigestDiff(toHex(digest.digest(readAllBytes(actual.toPath()))), expected, digest);
    // WHEN
    expectAssertionError(() -> unMockedFiles.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldHaveDigest(actual, digestDiff));
  }

  @Test
  void should_pass_if_actual_has_expected_digest() throws Exception {
    // GIVEN
    String algorithm = "MD5";
    byte[] data = "Content".getBytes();
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    writeByteArrayToFile(actual, data);
    String expected = toHex(MessageDigest.getInstance(algorithm).digest(data));
    // WHEN/THEN
    unMockedFiles.assertHasDigest(INFO, actual, algorithm, Digests.fromHex(expected));
  }

}
