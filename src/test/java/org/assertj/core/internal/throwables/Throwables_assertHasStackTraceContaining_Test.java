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
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldContainCharSequence.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Throwables#assertHasStackTraceContaining(AssertionInfo, Throwable, String)}</code>.
 * 
 * @author Daniel Zlotin
 */
public class Throwables_assertHasStackTraceContaining_Test extends ThrowablesBaseTest {

  @Test
  public void should_fail_if_actual_has_stacktrace_not_containing_the_expected_description() {
    final AssertionInfo info = someInfo();
    try {
      throwables.assertHasStackTraceContaining(info, actual, "expected description part");
      fail("AssertionError expected");
    } catch (final AssertionError err) {
      verify(failures).failure(info,
                               shouldContain(org.assertj.core.util.Throwables.getStackTrace(actual),
                                             "expected description part"));
    }
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    throwables.assertHasStackTraceContaining(someInfo(), null, "Throwable");
  }

  @Test
  public void should_pass_if_actual_has_stacktrace_containing_the_expected_description() {
    throwables.assertHasStackTraceContaining(someInfo(), actual, "able");
  }

}
