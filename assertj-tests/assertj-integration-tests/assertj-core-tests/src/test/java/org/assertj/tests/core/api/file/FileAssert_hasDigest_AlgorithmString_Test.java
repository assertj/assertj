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
package org.assertj.tests.core.api.file;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalStateException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.internal.Digests.toHex;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.assertj.core.internal.DigestDiff;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_hasDigest_AlgorithmString_Test {

  private static final String ALGORITHM = "MD5";
  private static final byte[] CONTENT = "Content".getBytes(UTF_8);

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_algorithm_is_null() throws IOException {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    String algorithm = null;
    String expected = "";
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(algorithm, expected));
    // THEN
    then(npe).hasMessage("The message digest algorithm should not be null");
  }

  @Test
  void should_throw_error_if_expected_is_null() throws IOException {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    String expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(npe).hasMessage("The string representation of digest to compare to should not be null");
  }

  @Test
  void should_throw_error_if_algorithm_is_unknown() throws IOException {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    String unknownDigestAlgorithm = "UnknownDigestAlgorithm";
    String expected = "";
    // WHEN
    var ise = catchIllegalStateException(() -> assertThat(actual).hasDigest(unknownDigestAlgorithm, expected));
    // THEN
    then(ise).hasMessage("Unable to find digest implementation for: <UnknownDigestAlgorithm>")
             .hasCauseInstanceOf(NoSuchAlgorithmException.class);
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(assertionError).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws IOException {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    actual.setReadable(false);
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_digest() throws Exception {
    // GIVEN
    File actual = createFileWithContent("Bad Content".getBytes(UTF_8));
    MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
    String expected = toHex(digest.digest(CONTENT));
    DigestDiff digestDiff = new DigestDiff(toHex(digest.digest(Files.readAllBytes(actual.toPath()))), expected, digest);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(ALGORITHM, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDigest(actual, digestDiff).create());
  }

  @Test
  void should_pass_if_actual_has_expected_digest() throws Exception {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    String expected = toHex(MessageDigest.getInstance(ALGORITHM).digest(CONTENT));
    // WHEN/THEN
    then(actual).hasDigest(ALGORITHM, expected);
  }

  private File createFileWithContent(byte[] content) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), content).toFile();
  }

}
