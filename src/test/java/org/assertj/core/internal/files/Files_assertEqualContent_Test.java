/*
 * Created on Jan 27, 2011
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
package org.assertj.core.internal.files;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;

import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveEqualContent.shouldHaveEqualContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.assertj.core.util.FilesException;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for <code>{@link Files#assertEqualContent(AssertionInfo, File, File)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Files_assertEqualContent_Test extends FilesBaseTest {

  private static File actual;
  private static File expected;

  @BeforeClass
  public static void setUpOnce() {
    actual = new File("src/test/resources/actual_file.txt");
    expected = new File("src/test/resources/expected_file.txt");
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The file to compare to should not be null");
    files.assertEqualContent(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_expected_is_not_file() {
    thrown.expectIllegalArgumentException("Expected file:<'xyz'> should be an existing file");
    File notAFile = new File("xyz");
    files.assertEqualContent(someInfo(), actual, notAFile);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    files.assertEqualContent(someInfo(), null, expected);
  }

  @Test
  public void should_fail_if_actual_is_not_file() {
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");
    try {
      files.assertEqualContent(info, notAFile, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(notAFile));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_files_have_equal_content() throws IOException {
    when(diff.diff(actual, expected)).thenReturn(new ArrayList<String>());
    files.assertEqualContent(someInfo(), actual, expected);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, expected)).thenThrow(cause);
    try {
      files.assertEqualContent(someInfo(), actual, expected);
      fail("Expected a FilesException to be thrown");
    } catch (FilesException e) {
      assertSame(cause, e.getCause());
    }
  }

  @Test
  public void should_fail_if_files_do_not_have_equal_content() throws IOException {
    List<String> diffs = newArrayList("line:1, expected:<line1> but was:<EOF>");
    when(diff.diff(actual, expected)).thenReturn(diffs);
    AssertionInfo info = someInfo();
    try {
      files.assertEqualContent(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveEqualContent(actual, expected, diffs));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
