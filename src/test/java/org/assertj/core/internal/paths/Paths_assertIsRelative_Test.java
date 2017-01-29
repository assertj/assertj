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
package org.assertj.core.internal.paths;

import static org.assertj.core.error.ShouldBeRelativePath.shouldBeRelativePath;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class Paths_assertIsRelative_Test extends MockPathsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	paths.assertIsRelative(info, null);
  }

  @Test
  public void should_fail_if_actual_is_not_relative() {
	// This is the default, but make it explicit
	when(actual.isAbsolute()).thenReturn(true);

	try {
	  paths.assertIsRelative(info, actual);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldBeRelativePath(actual));
	}
  }

  @Test
  public void should_pass_if_actual_is_relative() {
	when(actual.isAbsolute()).thenReturn(false);
	paths.assertIsRelative(info, actual);
  }
}
