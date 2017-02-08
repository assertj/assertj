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
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldNotEndWith.shouldNotEndWith;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotEndWith(AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Michal Kordas
 */
public class Strings_assertDoesNotEndWith_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_does_not_end_with_suffix() {
    strings.assertDoesNotEndWith(someInfo(), "Yoda", "Luke");
    strings.assertDoesNotEndWith(someInfo(), "Yoda", "DA");
  }

  @Test
  public void should_fail_if_actual_ends_with_suffix() {
    thrown.expectAssertionError(shouldNotEndWith("Yoda", "oda"));
    strings.assertDoesNotEndWith(someInfo(), "Yoda", "oda");
  }

  @Test
  public void should_throw_error_if_suffix_is_null() {
    thrown.expectNullPointerException("The given suffix should not be null");
    strings.assertDoesNotEndWith(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertDoesNotEndWith(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_does_not_end_with_suffix_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotEndWith(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_fail_if_actual_ends_with_suffix_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError(shouldNotEndWith("Yoda", "A", comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotEndWith(someInfo(), "Yoda", "A");
  }

}
