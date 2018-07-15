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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.Digests;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Paths#assertHasDigest(AssertionInfo, Path, String, byte[])}</code>
 *
 * @author Valeriy Vyrva
 */
public class Paths_assertHasDigest_AlgorithmBytes_Test extends MockPathsBaseTest {
  private final String algorithm = "MD5";
  private final byte[] expected = new byte[0];
  private final String real = "3AC1AFA2A89B7E4F1866502877BF1DC5";

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> paths.assertHasDigest(info, null, algorithm, expected))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_with_should_exist_error_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    catchThrowable(() -> paths.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldExist(actual));
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_file() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(false);
    // WHEN
    catchThrowable(() -> paths.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeRegularFile(actual));
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_readable() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(false);
    // WHEN
    catchThrowable(() -> paths.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeReadable(actual));
  }

  @Test
  public void should_throw_error_if_digest_is_null() {
    assertThatNullPointerException().isThrownBy(() -> paths.assertHasDigest(INFO, null, (MessageDigest) null, expected))
                                    .withMessage("The message digest algorithm should not be null");
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> paths.assertHasDigest(INFO, null, algorithm, (byte[]) null))
                                    .withMessage("The binary representation of digest to compare to should not be null");
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(true);
    given(nioFilesWrapper.newInputStream(actual)).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> paths.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    assertThat(error).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
  }

  @Test
  public void should_throw_error_wrapping_catched_NoSuchAlgorithmException() {
    // GIVEN
    String unknownDigestAlgorithm = "UnknownDigestAlgorithm";
    // WHEN
    Throwable error = catchThrowable(() -> paths.assertHasDigest(INFO, actual, unknownDigestAlgorithm, expected));
    // THEN
    assertThat(error).isInstanceOf(IllegalStateException.class)
                     .hasMessage("Unable to find digest implementation for: <UnknownDigestAlgorithm>");
  }

  @Test
  public void should_fail_if_actual_does_not_have_expected_digest() throws IOException, NoSuchAlgorithmException {
    // GIVEN
    InputStream stream = getClass().getResourceAsStream("/red.png");
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(true);
    given(nioFilesWrapper.newInputStream(actual)).willReturn(stream);
    // WHEN
    catchThrowable(() -> paths.assertHasDigest(INFO, actual, algorithm, expected));
    // THEN
    verify(failures).failure(INFO, shouldHaveDigest(actual, new DigestDiff(real, "", MessageDigest.getInstance(algorithm))));
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_pass_if_actual_has_expected_digest() throws IOException {
    // GIVEN
    InputStream stream = getClass().getResourceAsStream("/red.png");
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(true);
    given(nioFilesWrapper.newInputStream(actual)).willReturn(stream);
    // WHEN
    paths.assertHasDigest(INFO, actual, algorithm, Digests.fromHex(real));
    // THEN
    failIfStreamIsOpen(stream);
  }
}
