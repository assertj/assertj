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
package org.assertj.core.internal.paths;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.internal.BinaryDiffResult.noDiff;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Paths_assertHasSameBinaryContentAs_Test extends PathsBaseTest {

  private Path actual;
  private Path expected;
  private byte[] expectedBytes;

  @BeforeEach
  public void setUpOnce() throws IOException {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = createTempPathWithContent("foo", defaultCharset());
    expected = createTempPathWithContent("bar", defaultCharset());
    expectedBytes = readAllBytes(expected);
    when(nioFilesWrapper.exists(actual)).thenReturn(true);
    when(nioFilesWrapper.isReadable(actual)).thenReturn(true);
    when(nioFilesWrapper.exists(expected)).thenReturn(true);
    when(nioFilesWrapper.isReadable(expected)).thenReturn(true);
  }

  @Test
  public void should_pass_if_path_has_same_binary_content_as_expected() throws IOException {
    // GIVEN
    given(binaryDiff.diff(actual, expectedBytes)).willReturn(noDiff());
    // WHEN/THEN
    paths.assertHasSameBinaryContentAs(someInfo(), actual, expected);
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    // GIVEN
    Path nullExpected = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, nullExpected),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The given Path to compare actual content to should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Path path = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasSameBinaryContentAs(someInfo(), path, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_path_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldExist(actual));
  }

  @Test
  public void should_fail_if_actual_is_not_readable() {
    // GIVEN
    when(nioFilesWrapper.isReadable(actual)).thenReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeReadable(actual));
  }

  @Test
  public void should_fail_if_expected_path_is_does_not_exist() {
    // GIVEN
    when(nioFilesWrapper.exists(expected)).thenReturn(false);
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected),
                                                        IllegalArgumentException.class);
    // THEN
    then(iae).hasMessage("The given Path <%s> to compare actual content to should exist", expected);
  }

  @Test
  public void should_fail_if_expected_path_is_not_readable() {
    // GIVEN
    when(nioFilesWrapper.isReadable(expected)).thenReturn(false);
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected),
                                                        IllegalArgumentException.class);
    // THEN
    then(iae).hasMessage("The given Path <%s> to compare actual content to should be readable", expected);
  }

  @Test
  public void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(binaryDiff.diff(actual, expectedBytes)).willThrow(cause);
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  public void should_fail_if_path_does_not_have_expected_binary_content() throws IOException {
    // GIVEN
    BinaryDiffResult diff = new BinaryDiffResult(15, (byte) 0xCA, (byte) 0xFE);
    when(binaryDiff.diff(actual, expectedBytes)).thenReturn(diff);
    // WHEN
    expectAssertionError(() -> paths.assertHasSameBinaryContentAs(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldHaveBinaryContent(actual, diff));
  }
}
