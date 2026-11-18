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

import org.assertj.core.internal.DigestDiff;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_hasDigest_DigestBytes_Test {

  private static final byte[] CONTENT = "Content".getBytes(UTF_8);

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() throws Exception {
    // GIVEN
    File actual = null;
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = new byte[0];
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_digest_is_null() throws IOException {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    MessageDigest digest = null;
    byte[] expected = new byte[0];
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(npe).hasMessage("The message digest algorithm should not be null");
  }

  @Test
  void should_throw_error_if_expected_is_null() throws Exception {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(npe).hasMessage("The binary representation of digest to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws Exception {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = new byte[0];
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(assertionError).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws Exception {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = new byte[0];
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws Exception {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    actual.setReadable(false);
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = new byte[0];
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(assertionError).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_digest() throws Exception {
    // GIVEN
    File actual = createFileWithContent("Bad Content".getBytes(UTF_8));
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = digest.digest(CONTENT);
    DigestDiff digestDiff = new DigestDiff(toHex(digest.digest(Files.readAllBytes(actual.toPath()))), toHex(expected), digest);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(digest, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDigest(actual, digestDiff).create());
  }

  @Test
  void should_pass_if_actual_has_expected_digest() throws Exception {
    // GIVEN
    File actual = createFileWithContent(CONTENT);
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] expected = digest.digest(CONTENT);
    // WHEN/THEN
    then(actual).hasDigest(digest, expected);
  }

  private File createFileWithContent(byte[] content) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), content).toFile();
  }

}
