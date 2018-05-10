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
package org.assertj.core.internal.inputstreams;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsBaseTest;
import org.assertj.core.internal.InputStreamsException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;

import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link InputStreams#assertHasDigest(AssertionInfo, InputStream, MessageDigest, byte[])}</code>
 *
 * @author Valeriy Vyrva
 */
public class InputStreams_assertHasDigest_DigestBytes_Test extends InputStreamsBaseTest {
  private final MessageDigest digest = mock(MessageDigest.class);
  private final byte[] expected = new byte[0];

  @Test
  public void should_fail_if_actual_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectAssertionError(actualIsNull());
  inputStreams.assertHasDigest(info, null, digest, expected);
  }

  @Test
  public void should_throw_error_if_digest_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectNullPointerException("The message digest algorithm should not be null");
  inputStreams.assertHasDigest(info, null, (MessageDigest) null, expected);
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
  AssertionInfo info = someInfo();
  thrown.expectNullPointerException("The binary representation of digest to compare to should not be null");
  inputStreams.assertHasDigest(info, null, digest, (byte[]) null);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
  AssertionInfo info = someInfo();
  IOException cause = new IOException();
  actual = mock(InputStream.class);
  when(actual.read(any())).thenThrow(cause);
  thrown.expectWithCause(InputStreamsException.class, cause);
  inputStreams.assertHasDigest(info, actual, digest, expected);
  }

  @Test
  public void should_fail_if_actual_does_not_have_expected_digest() {
  AssertionInfo info = someInfo();
  actual = getClass().getResourceAsStream("/red.png");
  when(digest.digest()).thenReturn(new byte[]{0, 1});
  try {
    inputStreams.assertHasDigest(info, actual, digest, expected);
    wasExpectingAssertionError();
  } catch (AssertionError e) {
    verify(failures).failure(info, shouldHaveDigest(actual, new DigestDiff(digest, "", "0001")));
    return;
  }
  failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_digest() {
  AssertionInfo info = someInfo();
  actual = getClass().getResourceAsStream("/red.png");
  when(digest.digest()).thenReturn(expected);
  inputStreams.assertHasDigest(info, actual, digest, expected);
  }
}
