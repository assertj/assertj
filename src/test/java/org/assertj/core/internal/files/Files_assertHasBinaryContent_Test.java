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
package org.assertj.core.internal.files;

import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for <code>{@link Files#assertHasBinaryContent(org.assertj.core.core.WritableAssertionInfo, File, byte[])}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
public class Files_assertHasBinaryContent_Test extends FilesBaseTest {

  private static File actual;
  private static byte[] expected;

  @BeforeClass
  public static void setUpOnce() {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = new File("src/test/resources/actual_file.txt");
    expected = new byte[] {};
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException("The binary content to compare to should not be null");
    files.assertHasBinaryContent(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasBinaryContent(someInfo(), null, expected);
  }

  @Test
  public void should_fail_if_actual_is_not_file() {
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");
    try {
      files.assertHasBinaryContent(info, notAFile, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(notAFile));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_file_has_expected_binary_content() throws IOException {
    when(binaryDiff.diff(actual, expected)).thenReturn(BinaryDiffResult.noDiff());
    files.assertHasBinaryContent(someInfo(), actual, expected);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    IOException cause = new IOException();
    when(binaryDiff.diff(actual, expected)).thenThrow(cause);

    thrown.expectWithCause(RuntimeIOException.class, cause);

    files.assertHasBinaryContent(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_file_does_not_have_expected_binary_content() throws IOException {
    BinaryDiffResult diff = new BinaryDiffResult(15, (byte) 0xCA, (byte) 0xFE);
    when(binaryDiff.diff(actual, expected)).thenReturn(diff);
    AssertionInfo info = someInfo();
    try {
      files.assertHasBinaryContent(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveBinaryContent(actual, diff));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
