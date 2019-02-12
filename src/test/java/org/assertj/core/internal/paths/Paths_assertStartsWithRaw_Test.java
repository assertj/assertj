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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class Paths_assertStartsWithRaw_Test extends MockPathsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
	assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> paths.assertStartsWithRaw(info, null, other))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_other_is_null() {
    assertThatNullPointerException().isThrownBy(() -> paths.assertStartsWithRaw(info, actual, null))
                                    .withMessage("the expected start path should not be null");
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_other() {
	// This is the default, but let's make this explicit
	when(actual.startsWith(other)).thenReturn(false);

	try {
	  paths.assertStartsWithRaw(info, actual, other);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldStartWith(actual, other));
	}
  }

  @Test
  public void should_succeed_if_actual_starts_with_other() {
	when(actual.startsWith(other)).thenReturn(true);

	paths.assertStartsWithRaw(info, actual, other);
  }
}
