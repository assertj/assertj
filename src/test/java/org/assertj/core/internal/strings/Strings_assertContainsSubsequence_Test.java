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

import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldContainCharSequence.*;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.util.Arrays.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Sets.*;

public class Strings_assertContainsSubsequence_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_contains_Subsequence() {
    strings.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da"));
  }

  @Test
  public void should_pass_if_actual_contains_Subsequence_with_values_between() {
    String[] sequenceValues = { "{", "title", "A Game of Thrones", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    strings.assertContainsSubsequence(someInfo(), actual, sequenceValues);
  }
  
  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings() {
    thrown.expectAssertionError(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han")));
    strings.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da", "Han"));
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order() {
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    thrown.expectAssertionError(shouldContainSubsequence(actual, sequenceValues, 1));
    strings.assertContainsSubsequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_throw_error_if_sequence_values_is_null() {
    thrown.expectNullPointerException(arrayOfValuesToLookForIsNull());
    strings.assertContainsSubsequence(someInfo(), "Yoda", null);
  }

  @Test
  public void should_throw_error_if_Subsequence_values_is_empty() {
    thrown.expectIllegalArgumentException(arrayOfValuesToLookForIsEmpty());
    strings.assertContainsSubsequence(someInfo(), "Yoda", new String[0]);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsSubsequence(someInfo(), (CharSequence)null, array("Yo", "da"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_strings() {
    strings.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da"));
  }

  @Test
  public void should_pass_if_actual_contains_Subsequence_that_specifies_multiple_times_the_same_value_bug_544() {
    strings.assertContainsSubsequence(someInfo(), "a-b-c-", array("a", "-", "b", "-", "c"));
  }

  @Test
  public void should_pass_if_actual_contains_Subsequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("YO", "dA"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_Subsequence_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"),
                                              comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da", "Han"));
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    thrown.expectAssertionError(shouldContainSubsequence(actual, sequenceValues, 1, comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual, sequenceValues);
  }

}
