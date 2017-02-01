/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.inputstreams;

import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsBaseTest;
import org.assertj.core.internal.InputStreamsException;
import org.assertj.core.util.diff.Delta;
import org.junit.Test;


/**
 * Tests for <code>{@link InputStreams#assertSameContentAs(AssertionInfo, InputStream, InputStream)}</code>.
 * 
 * @author Matthieu Baechler
 */
public class InputStreams_assertSameContentAs_Test extends InputStreamsBaseTest {

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The InputStream to compare to should not be null");
    inputStreams.assertSameContentAs(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    inputStreams.assertSameContentAs(someInfo(), null, expected);
  }

  @Test
  public void should_pass_if_inputstreams_have_equal_content() throws IOException {
    when(diff.diff(actual, expected)).thenReturn(new ArrayList<Delta<String>>());
    inputStreams.assertSameContentAs(someInfo(), actual, expected);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, expected)).thenThrow(cause);

    thrown.expectWithCause(InputStreamsException.class, cause);

    inputStreams.assertSameContentAs(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_inputstreams_do_not_have_equal_content() throws IOException {
    @SuppressWarnings("unchecked")
    List<Delta<String>> diffs = newArrayList((Delta<String>) mock(Delta.class));
    when(diff.diff(actual, expected)).thenReturn(diffs);
    AssertionInfo info = someInfo();
    try {
      inputStreams.assertSameContentAs(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSameContent(actual, expected, diffs));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
