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

import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Strings#assertContainsIgnoringCase(AssertionInfo, CharSequence, CharSequence)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertContainsIgnoringCase_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_does_not_contain_sequence() {
    thrown.expectAssertionError(shouldContainIgnoringCase("Yoda", "Luke"));
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsIgnoringCase(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  public void should_pass_if_actual_contains_sequence_in_different_case() {
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_sequence_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(shouldContainIgnoringCase("Yoda", "Luke"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_contains_sequence_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  public void should_pass_if_actual_contains_sequence_in_different_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }
}
