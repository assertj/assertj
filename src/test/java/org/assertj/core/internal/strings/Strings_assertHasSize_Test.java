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

import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Strings#assertHasSize(AssertionInfo, CharSequence, int)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertHasSize_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertHasSize(someInfo(), null, 3);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String actual = "Han";

    thrown.expectAssertionError(shouldHaveSize(actual, actual.length(), 6).create());

    strings.assertHasSize(info, actual, 6);
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    strings.assertHasSize(someInfo(), "Han", 3);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertHasSize(someInfo(), null, 3);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    String actual = "Han";

    thrown.expectAssertionError(shouldHaveSize(actual, actual.length(), 6).create());

    stringsWithCaseInsensitiveComparisonStrategy.assertHasSize(info, actual, 6);
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertHasSize(someInfo(), "Han", 3);
  }
}
