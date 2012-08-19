/*
 * Created on Jan 29, 2011
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

import static org.fest.assertions.error.ShouldNotExist.shouldNotExist;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Files;
import org.fest.assertions.internal.FilesBaseTest;

/**
 * Tests for <code>{@link Files#assertDoesNotExist(AssertionInfo, File)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Files_assertDoesNotExist_Test extends FilesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    files.assertDoesNotExist(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_exists() {
    when(actual.exists()).thenReturn(true);
    AssertionInfo info = someInfo();
    try {
      files.assertDoesNotExist(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotExist(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_does_not_exist() {
    when(actual.exists()).thenReturn(false);
    files.assertDoesNotExist(someInfo(), actual);
  }
}
