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
import org.assertj.core.internal.FilesBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasExtension(org.assertj.core.api.AssertionInfo, java.io.File, String)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Files_assertHasExtension_Test extends FilesBaseTest {

  private String expectedExtension = "java";

  @Test
  public void should_throw_error_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasExtension(someInfo(), null, expectedExtension);
  }

  @Test
  public void should_throw_npe_if_extension_is_null() throws Exception {
    thrown.expectNullPointerException("The expected extension should not be null.");
    files.assertHasExtension(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_actual_is_not_a_file() throws Exception {
    AssertionInfo info = someInfo();
    when(actual.isFile()).thenReturn(false);
    try {
      files.assertHasExtension(info, actual, expectedExtension);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_actual_does_not_have_the_expected_extension() throws Exception {
    AssertionInfo info = someInfo();
    when(actual.isFile()).thenReturn(true);
    when(actual.getName()).thenReturn("file.png");
    try {
      files.assertHasExtension(info, actual, expectedExtension);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveExtension(actual, "png", expectedExtension));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_extension() throws Exception {
    when(actual.isFile()).thenReturn(true);
    when(actual.getName()).thenReturn("file.java");
    files.assertHasExtension(someInfo(), actual, expectedExtension);
  }
}
