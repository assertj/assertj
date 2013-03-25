/*
 * Created on Dec 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldContainString.shouldContain;
import static org.assertj.core.error.ShouldContainStringSequence.shouldContainSequence;
import static org.assertj.core.test.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.test.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;

/**
 * Tests for <code>{@link Strings#assertContains(AssertionInfo, String, String)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertContainsSequence_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_contains_sequence() {
    strings.assertContainsSequence(someInfo(), "Yoda", array("Yo", "da"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContainsSequence(info, "Yoda", array("Yo", "da", "Han"));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order() {
    AssertionInfo info = someInfo();
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    try {
      strings.assertContainsSequence(info, actual, sequenceValues);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequenceValues, 1));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_sequence_values_is_null() {
    thrown.expectNullPointerException(arrayOfValuesToLookForIsNull());
    strings.assertContainsSequence(someInfo(), "Yoda", null);
  }

  @Test
  public void should_throw_error_if_sequence_values_is_empty() {
    thrown.expectIllegalArgumentException(arrayOfValuesToLookForIsEmpty());
    strings.assertContainsSequence(someInfo(), "Yoda", new String[0]);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContains(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_contains_all_given_strings() {
    strings.assertContains(someInfo(), "Yoda", "Yo", "da");
  }

  // tests with custom comparison strategy

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", array("YO", "dA"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, "Yoda", array("Yo", "da", "Han"));
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"),
                                             comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, actual, sequenceValues);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequenceValues, 1, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
