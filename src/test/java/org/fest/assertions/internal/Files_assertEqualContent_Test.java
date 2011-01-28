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
package org.fest.assertions.internal;

import static junit.framework.Assert.*;
import static org.fest.assertions.error.ShouldBeFile.shouldBeFile;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.fest.util.Files.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.ArrayList;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.fest.util.FilesException;
import org.junit.*;

/**
 * Tests for <code>{@link Files#assertEqualContent(AssertionInfo, File, File)}</code>.
 *
 * @author Yvonne Wang
 */
public class Files_assertEqualContent_Test {

  private static File actual;
  private static File expected;

  @BeforeClass public static void setUpOnce() {
    actual = newTemporaryFile();
    expected = newTemporaryFile();
  }

  @AfterClass public static void tearDownOnce() {
    delete(actual);
    delete(expected);
  }

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Diff diff;
  private Files files;

  @Before public void setUp() {
    failures = spy(new Failures());
    diff = mock(Diff.class);
    files = new Files();
    files.failures = failures;
    files.diff = diff;
  }

  @Test public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The file to compare to should not be null");
    files.assertEqualContent(someInfo(), actual, null);
  }

  @Test public void should_throw_error_if_expected_is_not_file() {
    thrown.expectIllegalArgumentException("Expected file:<'xyz'> should be an existing file");
    files.assertEqualContent(someInfo(), actual, new File("xyz"));
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    files.assertEqualContent(someInfo(), null, expected);
  }

  @Test public void should_fail_if_actual_is_not_file() {
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");
    try {
      files.assertEqualContent(info, notAFile, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(notAFile));
      return;
    }
    expectedAssertionErrorNotThrown();
  }

  @Test public void should_pass_if_files_have_equal_content() throws IOException {
    when(diff.diff(actual, expected)).thenReturn(new ArrayList<String>());
    files.assertEqualContent(someInfo(), actual, expected);
  }

  @Test public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(diff.diff(actual, expected)).thenThrow(cause);
    try {
      files.assertEqualContent(someInfo(), actual, expected);
      fail("Expected a FilesException to be thrown");
    } catch (FilesException e) {
      assertSame(cause, e.getCause());
    }
  }
}
