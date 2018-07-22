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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsBaseTest;
import org.assertj.core.internal.InputStreamsException;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link InputStreams#assertHasContent(AssertionInfo, InputStream, String)}</code>.
 *
 * @author Stephan Windmüller
 */
public class InputStreams_assertHasContent_Test extends InputStreamsBaseTest {

  @Test
  public void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> inputStreams.assertHasContent(someInfo(), actual, null))
                                    .withMessage("The String to compare to should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> inputStreams.assertHasContent(someInfo(), null, ""))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_inputstream_and_string_have_equal_content() throws IOException {
    // GIVEN
    given(diff.diff(actual, expected)).willReturn(emptyList());
    // THEN
    inputStreams.assertHasContent(someInfo(), actual, expectedString);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(diff.diff(actual, expectedString)).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> inputStreams.assertHasContent(someInfo(), actual, expectedString));
    // THEN
    assertThat(error).isInstanceOf(InputStreamsException.class)
                     .hasCause(cause);
  }

  @Test
  public void should_fail_if_inputstream_and_string_do_not_have_equal_content() throws IOException {
    // GIVEN
    List<Delta<String>> diffs = list((Delta<String>) mock(Delta.class));
    given(diff.diff(actual, expectedString)).willReturn(diffs);
    AssertionInfo info = someInfo();
    // WHEN
    catchThrowable(() -> inputStreams.assertHasContent(someInfo(), actual, expectedString));
    // THEN
    verify(failures).failure(info, shouldHaveSameContent(actual, expectedString, diffs));
  }
}
