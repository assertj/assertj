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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasParent(org.assertj.core.api.AssertionInfo, java.io.File, java.io.File)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Files_assertHasParent_Test extends FilesBaseTest {

  private File actual = new File("./some/test");
  private File expectedParent = new File("./some");

  @Test
  void should_throw_error_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasParent(INFO, actual, expectedParent));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_npe_if_expected_is_null() {
    // GIVEN
    File expected = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> underTest.assertHasParent(INFO, actual, expected),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The expected parent file should not be null.");
  }

  @Test
  void should_fail_if_actual_has_no_parent() {
    // GIVEN
    File withoutParent = new File("without-parent");
    // WHEN
    expectAssertionError(() -> underTest.assertHasParent(INFO, withoutParent, expectedParent));
    // THEN
    verify(failures).failure(INFO, shouldHaveParent(withoutParent, expectedParent));
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_parent() {
    // GIVEN
    File expectedParent = new File("./expected-parent");
    // WHEN
    expectAssertionError(() -> underTest.assertHasParent(INFO, actual, expectedParent));
    // THEN
    verify(failures).failure(INFO, shouldHaveParent(actual, expectedParent));
  }

  @Test
  void should_pass_if_actual_has_expected_parent() {
    underTest.assertHasParent(INFO, actual, expectedParent);
  }

  @Test
  void should_pass_if_actual_has_expected_parent_when_actual_form_is_absolute() {
    underTest.assertHasParent(INFO, actual.getAbsoluteFile(), expectedParent);
  }

  @Test
  void should_pass_if_actual_has_expected_parent_when_actual_form_is_canonical() throws Exception {
    underTest.assertHasParent(INFO, actual.getCanonicalFile(), expectedParent);
  }

  @Test
  void should_throw_exception_when_canonical_form_representation_fail() throws Exception {
    // GIVEN
    File actual = mock(File.class);
    File actualParent = mock(File.class);
    when(actual.getParentFile()).thenReturn(actualParent);
    when(actualParent.getCanonicalFile()).thenThrow(new IOException());
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> underTest.assertHasParent(INFO, actual, actualParent),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasMessageStartingWith("Unable to get canonical form of");
  }

  @Test
  void should_throw_exception_when_canonical_form_representation_fail_for_expected_parent() throws Exception {
    File expectedParent = mock(File.class);
    when(expectedParent.getCanonicalFile()).thenThrow(new IOException());
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> underTest.assertHasParent(INFO, actual, expectedParent),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasMessageStartingWith("Unable to get canonical form of");
  }
}
