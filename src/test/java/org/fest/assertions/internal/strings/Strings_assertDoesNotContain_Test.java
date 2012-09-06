/*
 * Created on Dec 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.strings;

import static org.fest.assertions.error.ShouldNotContainString.shouldNotContain;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.sequenceToLookForIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.StringsBaseTest;
import org.fest.assertions.internal.Strings;

/**
 * Tests for <code>{@link Strings#assertDoesNotContain(AssertionInfo, String, String)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_assertDoesNotContain_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_contains_sequence() {
    AssertionInfo info = someInfo();
    try {
      strings.assertDoesNotContain(info, "Yoda", "oda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain("Yoda", "oda"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(sequenceToLookForIsNull());
    strings.assertDoesNotContain(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertDoesNotContain(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_does_not_contain_sequence() {
    strings.assertDoesNotContain(someInfo(), "Yoda", "Lu");
  }

  @Test
  public void should_pass_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(someInfo(), "Yoda", "Lu");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(info, "Yoda", "yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain("Yoda", "yoda", comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
