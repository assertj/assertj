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
 * Copyright 2012-2017 the original author or authors.
 */

package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldContainCharSequence.*;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.util.Arrays.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Sets.*;

/**
 * Tests for <code>{@link Strings#assertContainsSequence(AssertionInfo, CharSequence, CharSequence[])}</code>.
 *
 * @author Billy Yuan
 */
public class Strings_assertContainsSequence_Test extends StringsBaseTest {
  String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

  @Test
  public void should_pass_if_actual_contains_Sequence() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "," };
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_fail_if_actual_contains_Sequence_with_values_between() {
    String[] sequenceValues = { "{ ", "'author':'George Martin'}" };
    thrown.expectAssertionError(shouldContainSequence(actual, sequenceValues));
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "unexpectedString" };
    thrown.expectAssertionError(shouldContain(actual, sequenceValues, newLinkedHashSet("unexpectedString")));
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_the_given_order() {
    String[] sequenceValues = { "'A Game of Thrones'", "'title':" };
    thrown.expectAssertionError(shouldContainSequence(actual, sequenceValues));
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_throw_error_if_Sequence_is_null() {
    thrown.expectNullPointerException(arrayOfValuesToLookForIsNull());
    strings.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_any_value_of_Sequence_is_null() {
    String[] sequenceValues = { "author", null };
    thrown.expectNullPointerException("Expecting CharSequence elements not to be null but found one at index 1");
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_throw_error_if_Sequence_values_are_empty() {
    thrown.expectIllegalArgumentException(arrayOfValuesToLookForIsEmpty());
    strings.assertContainsSequence(someInfo(), actual, new String[0]);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "," };
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsSequence(someInfo(), null, sequenceValues);
  }

  @Test
  public void should_pass_if_actual_contains_Sequence_that_specifies_multiple_times_the_same_value() {
    strings.assertContainsSequence(someInfo(), "a-b-c-", array("a", "-", "b", "-", "c"));
  }

  @Test
  public void should_pass_if_actual_contains_Sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("YO", "dA"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_Sequence_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"),
                                              comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy
      .assertContainsSequence(someInfo(), "Yoda", array("Yo", "da", "Han"));
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    String[] sequenceValues = { ", 'author'", "'A Game of Thrones'" };
    thrown.expectAssertionError(
      shouldContainSequence(actual, sequenceValues, comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), actual, sequenceValues);
  }
}
