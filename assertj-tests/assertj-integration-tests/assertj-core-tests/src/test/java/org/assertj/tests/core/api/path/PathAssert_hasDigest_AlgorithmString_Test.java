/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalStateException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.internal.Digests.toHex;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.assertj.core.internal.DigestDiff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.io.TempDir;

class PathAssert_hasDigest_AlgorithmString_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_algorithm_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = null;
    String expected = "";
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(npe).hasMessage("The message digest algorithm should not be null");
  }

  @Test
  void should_fail_if_algorithm_is_invalid() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = "invalid";
    String expected = "";
    // WHEN
    var illegalStateException = catchIllegalStateException(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(illegalStateException).hasMessage("Unable to find digest implementation for: <invalid>")
                               .hasCauseInstanceOf(NoSuchAlgorithmException.class);
  }

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String algorithm = "MD5";
    String expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(npe).hasMessage("The string representation of digest to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(assertionError).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_regular_file() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("directory"));
    String algorithm = "MD5";
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeRegularFile(actual).create());
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
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_digest() throws Exception {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    String algorithm = "MD5";
    MessageDigest digest = MessageDigest.getInstance(algorithm);
    String expected = toHex(digest.digest("Another content".getBytes()));
    DigestDiff digestDiff = new DigestDiff(toHex(digest.digest(readAllBytes(actual))), expected, digest);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDigest(actual, digestDiff).create());
  }

  @Test
  void should_pass_if_actual_has_expected_digest() throws Exception {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    String algorithm = "MD5";
    String expected = toHex(MessageDigest.getInstance(algorithm).digest(readAllBytes(actual)));
    // WHEN/THEN
    then(actual).hasDigest(algorithm, expected);
  }

}
