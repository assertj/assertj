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

import java.io.File;

import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasNoParent(org.assertj.core.api.AssertionInfo, java.io.File)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Files_assertHasNoParent_Test extends FilesBaseTest {

  @Test
  public void should_throw_error_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasNoParent(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_has_parent() throws Exception {
    AssertionInfo info = someInfo();
    when(actual.getParentFile()).thenReturn(mock(File.class));
    try {
      files.assertHasNoParent(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParent(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_no_parent() throws Exception {
    when(actual.getParentFile()).thenReturn(null);
    files.assertHasNoParent(someInfo(), actual);
  }
}
