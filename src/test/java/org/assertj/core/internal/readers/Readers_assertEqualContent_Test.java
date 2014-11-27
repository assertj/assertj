/*
 * Created on Jan 27, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.internal.readers;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;
import static org.assertj.core.error.ShouldHaveEqualContent.shouldHaveEqualContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ReadersBaseTest;
import org.assertj.core.internal.ReadersException;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Readers#assertEqualContent(org.assertj.core.api.AssertionInfo, java.io.Reader, java.io.Reader)}</code>
 * .
 * 
 * @author Matthieu Baechler
 * @author Bartosz Bierkowski
 */
public class Readers_assertEqualContent_Test extends ReadersBaseTest {

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The Reader to compare to should not be null");
    readers.assertEqualContent(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    readers.assertEqualContent(someInfo(), null, expected);
  }

  @Test
  public void should_pass_if_readerss_have_equal_content() throws IOException {
    when(diff.diff(actual, expected)).thenReturn(new ArrayList<String>());
    readers.assertEqualContent(someInfo(), actual, expected);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, expected)).thenThrow(cause);
    try {
      readers.assertEqualContent(someInfo(), actual, expected);
      fail("Expected a ReadersException to be thrown");
    } catch (ReadersException e) {
      assertSame(cause, e.getCause());
    }
  }

  @Test
  public void should_fail_if_inputstreams_do_not_have_equal_content() throws IOException {
    List<String> diffs = newArrayList("line:1, expected:<line1> but was:<EOF>");
    when(diff.diff(actual, expected)).thenReturn(diffs);
    AssertionInfo info = someInfo();
    try {
      readers.assertEqualContent(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveEqualContent(actual, expected, diffs));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
