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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.test.ErrorMessages.charSequenceToLookForIsNull;
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
 * Tests for <code>{@link Strings#assertContains(AssertionInfo, CharSequence, CharSequence)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertContains_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_does_not_contain_sequence() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContains(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", "Luke"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_sequence_but_in_different_case() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContains(info, "Yoda", "yo");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", "yo"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    strings.assertContains(someInfo(), "Yoda", (String) null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContains(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    strings.assertContains(someInfo(), "Yoda", "Yo");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContains(info, "Yoda", "Yo", "da", "Han");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_all_given_strings() {
    strings.assertContains(someInfo(), "Yoda", "Yo", "da");
  }

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "Yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "YO");
  }

  @Test
  public void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "YO", "dA");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContains(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", "Luke", comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContains(info, "Yoda", "Yo", "da", "Han");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
