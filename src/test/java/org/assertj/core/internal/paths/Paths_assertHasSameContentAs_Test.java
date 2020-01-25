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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.paths;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Paths#assertHasSameContentAs(AssertionInfo, Path, Charset, Path, Charset)}</code>.
 */
public class Paths_assertHasSameContentAs_Test extends MockPathsBaseTest {

  private static final Charset CHARSET = defaultCharset();
  private Path actual;
  private Path expected;

  @BeforeEach
  public void setUpOnce() throws IOException {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = createTempPathWithContent("foo", CHARSET);
    expected = createTempPathWithContent("bar", CHARSET);
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(true);
    given(nioFilesWrapper.exists(expected)).willReturn(true);
    given(nioFilesWrapper.isReadable(expected)).willReturn(true);
  }

  @Test
  public void should_pass_if_path_has_same_textual_content_as_expected() throws IOException {
    // GIVEN
    given(diff.diff(actual, CHARSET, expected, CHARSET)).willReturn(emptyList());
    // WHEN/THEN
    paths.assertHasSameContentAs(someInfo(), actual, CHARSET, expected, CHARSET);
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    // GIVEN
    Path nullExpected = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET, nullExpected,
                                                                                       CHARSET),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The given Path to compare actual content to should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Path path = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasSameContentAs(someInfo(), path, CHARSET, expected,
                                                                                   CHARSET));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_path_does_not_exist() {
    // GIVEN
    AssertionInfo info = someInfo();
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertHasSameContentAs(info, actual, CHARSET, expected, CHARSET));
    // THEN
    verify(failures).failure(info, shouldExist(actual));
  }

  @Test
  public void should_fail_if_actual_is_not_a_readable_file() {
    // GIVEN
    given(nioFilesWrapper.isReadable(actual)).willReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET, expected, CHARSET));
    // THEN
    verify(failures).failure(someInfo(), shouldBeReadable(actual));
  }

  @Test
  public void should_fail_if_expected_path_is_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(expected)).willReturn(false);
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET,
                                                                                           expected, CHARSET),
                                                        IllegalArgumentException.class);
    // THEN
    then(iae).hasMessage("The given Path <%s> to compare actual content to should exist", expected);
  }

  @Test
  public void should_fail_if_expected_path_is_not_readable() {
    // GIVEN
    given(nioFilesWrapper.isReadable(expected)).willReturn(false);
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET,
                                                                                           expected, CHARSET),
                                                        IllegalArgumentException.class);
    // THEN
    then(iae).hasMessage("The given Path <%s> to compare actual content to should be readable", expected);
  }

  @Test
  public void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(diff.diff(actual, CHARSET, expected, CHARSET)).willThrow(cause);
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET,
                                                                                        expected, CHARSET),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  public void should_fail_if_actual_and_given_path_does_not_have_the_same_content() throws IOException {
    // GIVEN
    List<Delta<CharSequence>> diffs = list((Delta<CharSequence>) mock(Delta.class));
    given(diff.diff(actual, CHARSET, expected, CHARSET)).willReturn(diffs);
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> paths.assertHasSameContentAs(someInfo(), actual, CHARSET, expected, CHARSET));
    // THEN
    verify(failures).failure(info, shouldHaveSameContent(actual, expected, diffs));
  }
}
