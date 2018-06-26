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
package org.assertj.core.internal.files;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.Digests;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for <code>{@link Files#assertHasDigest(AssertionInfo, File, MessageDigest, String)}</code>
 *
 * @author Valeriy Vyrva
 */
public class Files_assertHasDigest_AlgorithmString_Test extends FilesBaseTest {
  private final String algorithm = "MD5";
  private final String expected = "";
  private final String real = "3AC1AFA2A89B7E4F1866502877BF1DC5";

  @Test
  public void should_fail_if_actual_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectAssertionError(actualIsNull());
  files.assertHasDigest(info, null, algorithm, expected);
  }

  @Test
  public void should_fail_with_should_exist_error_if_actual_does_not_exist() {
  AssertionInfo info = someInfo();
  when(actual.exists()).thenReturn(false);
  try {
    files.assertHasDigest(info, actual, algorithm, expected);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldExist(actual));
	  return;
	}
  failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_file() {
  AssertionInfo info = someInfo();
  when(actual.exists()).thenReturn(true);
  when(actual.isFile()).thenReturn(false);
  try {
    files.assertHasDigest(info, actual, algorithm, expected);
    wasExpectingAssertionError();
  } catch (AssertionError e) {
    verify(failures).failure(info, shouldBeFile(actual));
    return;
  }
  failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_readable() {
  AssertionInfo info = someInfo();
  when(actual.exists()).thenReturn(true);
  when(actual.isFile()).thenReturn(true);
  when(actual.canRead()).thenReturn(false);
  try {
    files.assertHasDigest(info, actual, algorithm, expected);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldBeReadable(actual));
	  return;
	}
  failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_digest_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectNullPointerException("The message digest algorithm should not be null");
  files.assertHasDigest(info, null, (MessageDigest) null, expected);
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectNullPointerException("The binary representation of digest to compare to should not be null");
  files.assertHasDigest(info, null, algorithm, (byte[]) null);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
  AssertionInfo info = someInfo();
  IOException cause = new IOException();
  when(actual.exists()).thenReturn(true);
  when(actual.isFile()).thenReturn(true);
  when(actual.canRead()).thenReturn(true);
  when(nioFilesWrapper.newInputStream(any())).thenThrow(cause);
  thrown.expectWithCause(UncheckedIOException.class, cause);
  files.assertHasDigest(info, actual, algorithm, expected);
  }

  @Test
  public void should_throw_error_wrapping_catched_NoSuchAlgorithmException() {
  AssertionInfo info = someInfo();
  thrown.expect(IllegalStateException.class, "Unable to find digest implementation for: <UnknownDigestAlgorithm>");
  files.assertHasDigest(info, actual, "UnknownDigestAlgorithm", expected);
  }

  private void failIfStreamIsOpen(InputStream stream) {
  try {
    assertThat(stream.read()).isNegative();
  } catch (IOException e) {
    assertThat(e).hasNoCause().hasMessage("Stream closed");
  }
  }

  @Test
  public void should_fail_if_actual_does_not_have_expected_digest() throws IOException, NoSuchAlgorithmException {
  AssertionInfo info = someInfo();
  InputStream stream = getClass().getResourceAsStream("/red.png");
  when(actual.exists()).thenReturn(true);
  when(actual.isFile()).thenReturn(true);
  when(actual.canRead()).thenReturn(true);
  when(nioFilesWrapper.newInputStream(any())).thenReturn(stream);
  try {
    files.assertHasDigest(info, actual, algorithm, expected);
    wasExpectingAssertionError();
  } catch (AssertionError e) {
    verify(failures).failure(info, shouldHaveDigest(actual, new DigestDiff(MessageDigest.getInstance(algorithm), "", real)));
    failIfStreamIsOpen(stream);
    return;
  }
  failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_digest() throws IOException {
  AssertionInfo info = someInfo();
  InputStream stream = getClass().getResourceAsStream("/red.png");
  when(actual.exists()).thenReturn(true);
  when(actual.isFile()).thenReturn(true);
  when(actual.canRead()).thenReturn(true);
  when(nioFilesWrapper.newInputStream(any())).thenReturn(stream);
  files.assertHasDigest(info, actual, algorithm, Digests.fromHex(real));
  failIfStreamIsOpen(stream);
  }
}
