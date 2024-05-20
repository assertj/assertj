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
package org.assertj.core.internal.paths;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.internal.Digests.toHex;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.WINDOWS;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

/**
 * @author Valeriy Vyrva
 */
class Paths_assertHasDigest_with_String_and_String_Test extends PathsBaseTest {

  @Test
  void should_fail_if_algorithm_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = null;
    String expected = "";
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The message digest algorithm should not be null");
  }

  @Test
  void should_fail_if_algorithm_is_invalid() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = "invalid";
    String expected = "";
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(thrown).isInstanceOf(IllegalStateException.class)
                .hasMessage("Unable to find digest implementation for: <invalid>")
                .hasCauseInstanceOf(NoSuchAlgorithmException.class);
  }

  @Test
  void should_fail_if_expected_is_null() throws Exception {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = "MD5";
    String expected = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The string representation of digest to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() throws NoSuchAlgorithmException {
    // GIVEN
    Path actual = null;
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws NoSuchAlgorithmException {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_regular_file() throws Exception {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("directory"));
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(shouldBeRegularFile(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    actual.toFile().setReadable(false);
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws Exception {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = "MD5";
    String expected = "";
    IOException cause = new IOException("boom!");
    willThrow(cause).given(nioFilesWrapper).newInputStream(actual);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(cause);
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_digest() throws Exception {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    String algorithm = "MD5";
    MessageDigest digest = MessageDigest.getInstance(algorithm);
    String expected = toHex(digest.digest("Another content".getBytes()));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    then(error).hasMessage(shouldHaveDigest(actual, new DigestDiff(toHex(digest.digest(readAllBytes(actual))),
                                                                   expected,
                                                                   digest)).create());
  }

  @Test
  void should_pass_if_actual_has_expected_digest() throws Exception {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    String algorithm = "MD5";
    String expected = toHex(MessageDigest.getInstance(algorithm).digest(readAllBytes(actual)));
    // WHEN/THEN
    underTest.assertHasDigest(INFO, actual, algorithm, expected);
  }

}
