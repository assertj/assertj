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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.inputstreams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.internal.BinaryDiffResult.noDiff;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsBaseTest;
import org.assertj.core.internal.InputStreamsException;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link InputStreams#assertHasBinaryContent(AssertionInfo, InputStream, byte[])}</code>.
 */
public class InputStreams_assertHasBinaryContent_Test extends InputStreamsBaseTest {

  @Test
  public void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> inputStreams.assertHasBinaryContent(someInfo(), actual, null))
                                    .withMessage("The binary content to compare to should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> inputStreams.assertHasBinaryContent(someInfo(), null, new byte[0]))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_inputstream_has_expected_binary_content() throws IOException {
    // GIVEN
    given(binaryDiff.diff(actual, expectedContent)).willReturn(noDiff());
    // THEN
    inputStreams.assertHasBinaryContent(someInfo(), actual, expectedContent);
  }

  @Test
  public void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(binaryDiff.diff(actual, expectedContent)).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> inputStreams.assertHasBinaryContent(someInfo(), actual, expectedContent));
    // THEN
    assertThat(error).isInstanceOf(InputStreamsException.class)
                     .hasCause(cause);
  }

  @Test
  public void should_fail_if_inputstream_and_string_do_not_have_equal_content() throws IOException {
    // GIVEN
    BinaryDiffResult diff = new BinaryDiffResult(1, 2, 3);
    given(binaryDiff.diff(actual, expectedContent)).willReturn(diff);
    AssertionInfo info = someInfo();
    // WHEN
    catchThrowable(() -> inputStreams.assertHasBinaryContent(someInfo(), actual, new byte[0]));
    // THEN
    verify(failures).failure(info, shouldHaveBinaryContent(actual, diff));
  }
}
