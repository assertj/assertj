/*
 * Created on Jan 29, 2011
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

import static org.fest.assertions.error.ShouldBeFile.shouldBeFile;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import java.io.File;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Files#assertIsFile(AssertionInfo, File)}</code>.
 *
 * @author Yvonne Wang
 */
public class Files_assertIsFile_Test {

  @Rule public ExpectedException thrown = none();

  private File actual;
  private Failures failures;
  private Files files;

  @Before public void setUp() {
    actual = mock(File.class);
    failures = spy(new Failures());
    files = new Files();
    files.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    files.assertIsFile(someInfo(), null);
  }

  @Test public void should_fail_if_actual_is_not_file() {
    when(actual.isFile()).thenReturn(false);
    AssertionInfo info = someInfo();
    try {
      files.assertIsFile(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFile(actual));
      return;
    }
    expectedAssertionErrorNotThrown();
  }

  @Test public void should_pass_if_actual_is_file() {
    when(actual.isFile()).thenReturn(true);
    files.assertIsFile(someInfo(), actual);
  }
}
