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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasParent(org.assertj.core.api.AssertionInfo, java.io.File, java.io.File)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Files_assertHasParent_Test extends FilesBaseTest {

  private File actual = new File("./some/test");
  private File expectedParent = new File("./some");

  @Test
  public void should_throw_error_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasParent(someInfo(), null, expectedParent);
  }

  @Test
  public void should_throw_npe_if_expected_is_null() throws Exception {
    thrown.expectNullPointerException("The expected parent file should not be null.");
    files.assertHasParent(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_has_no_parent() throws Exception {
    AssertionInfo info = someInfo();
    File withoutParent = new File("without-parent");
    try {
      files.assertHasParent(info, withoutParent, expectedParent);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParent(withoutParent, expectedParent));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_have_the_expected_parent() throws Exception {
    AssertionInfo info = someInfo();
    File expectedParent = new File("./expected-parent");
    try {
      files.assertHasParent(info, actual, expectedParent);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParent(actual, expectedParent));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_parent() throws Exception {
    files.assertHasParent(someInfo(), actual, expectedParent);
  }

  @Test
  public void should_pass_if_actual_has_expected_parent_when_actual_form_is_absolute() throws Exception {
    files.assertHasParent(someInfo(), actual.getAbsoluteFile(), expectedParent);
  }

  @Test
  public void should_pass_if_actual_has_expected_parent_when_actual_form_is_canonical() throws Exception {
    files.assertHasParent(someInfo(), actual.getCanonicalFile(), expectedParent);
  }

  @Test
  public void should_throw_exception_when_canonical_form_representation_fail() throws Exception {
    thrown.expect(RuntimeIOException.class);

    File actual = mock(File.class);
    File expectedParent = mock(File.class);

    when(actual.getParentFile()).thenReturn(expectedParent);
    when(expectedParent.getCanonicalFile()).thenThrow(new IOException());

    files.assertHasParent(someInfo(), actual, expectedParent);
  }

  @Test
  public void should_throw_exception_when_canonical_form_representation_fail_for_expected_parent() throws Exception {
    thrown.expect(RuntimeIOException.class);

    File expectedParent = mock(File.class);
    when(expectedParent.getCanonicalFile()).thenThrow(new IOException());

    files.assertHasParent(someInfo(), actual, expectedParent);
  }
}
