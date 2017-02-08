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

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Strings#assertEmpty(AssertionInfo, CharSequence)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertEmpty_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertEmpty(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_empty() {
    thrown.expectAssertionError(shouldBeEmpty("Yoda"));
    strings.assertEmpty(someInfo(), "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    strings.assertEmpty(someInfo(), "");
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertEmpty(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(shouldBeEmpty("Yoda"));
    stringsWithCaseInsensitiveComparisonStrategy.assertEmpty(someInfo(), "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEmpty(someInfo(), "");
  }
}
