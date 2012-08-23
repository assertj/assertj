/*
 * Created on Jul 21, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.files;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;

import static org.fest.assertions.error.ShouldBeFile.shouldBeFile;
import static org.fest.assertions.error.ShouldHaveContent.shouldHaveContent;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Files;
import org.fest.assertions.internal.FilesBaseTest;
import org.fest.util.FilesException;

/**
 * Tests for <code>{@link Files#assertHasContent(AssertionInfo, File, String, Charset)}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
public class Files_assertHasContent_Test extends FilesBaseTest {

  private static File actual;
  private static String expected;
  private static Charset charset;

  @BeforeClass
  public static void setUpOnce() {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = new File("src/test/resources/actual_file.txt");
    expected = "xyz";
    charset = Charset.defaultCharset();
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The text to compare to should not be null");
    files.assertHasContent(someInfo(), actual, null, charset);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasContent(someInfo(), null, expected, charset);
  }

  @Test
  public void should_fail_if_actual_is_not_file() {
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");
    try {
      files.assertHasContent(info, notAFile, expected, charset);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(notAFile));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_file_has_text_content() throws IOException {
    when(diff.diff(actual, expected, charset)).thenReturn(new ArrayList<String>());
    files.assertHasContent(someInfo(), actual, expected, charset);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, expected, charset)).thenThrow(cause);
    try {
      files.assertHasContent(someInfo(), actual, expected, charset);
      fail("Expected a FilesException to be thrown");
    } catch (FilesException e) {
      assertSame(cause, e.getCause());
    }
  }

  @Test
  public void should_fail_if_file_does_not_have_expected_text_content() throws IOException {
    List<String> diffs = newArrayList("line:1, expected:<line1> but was:<EOF>");
    when(diff.diff(actual, expected, charset)).thenReturn(diffs);
    AssertionInfo info = someInfo();
    try {
      files.assertHasContent(info, actual, expected, charset);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveContent(actual, charset, diffs));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
