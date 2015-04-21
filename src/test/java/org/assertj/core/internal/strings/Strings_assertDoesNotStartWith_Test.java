/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotStartWith(AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Michal Kordas
 */
public class Strings_assertDoesNotStartWith_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_starts_with_prefix() {
    AssertionInfo info = someInfo();
    try {
      strings.assertDoesNotStartWith(info, "Yoda", "Yo");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotStartWith("Yoda", "Yo"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_prefix_is_null() {
    thrown.expectNullPointerException("The given prefix should not be null");
    strings.assertDoesNotStartWith(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertDoesNotStartWith(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_does_not_start_with_prefix() {
    strings.assertDoesNotStartWith(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_pass_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotStartWith(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_fail_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotStartWith(info, "Yoda", "yODA");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotStartWith("Yoda", "yODA", comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
