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
package org.assertj.core.internal.characters;

import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Characters;
import org.assertj.core.internal.CharactersBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Characters#assertLessThan(AssertionInfo, Character, char)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Characters_assertLessThan_Test extends CharactersBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    characters.assertLessThan(someInfo(), null, 'a');
  }

  @Test
  public void should_pass_if_actual_is_less_than_other() {
    characters.assertLessThan(someInfo(), 'a', 'b');
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other() {
    AssertionInfo info = someInfo();
    try {
      characters.assertLessThan(info, 'b', 'b');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess('b', 'b'));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();
    try {
      characters.assertLessThan(info, 'b', 'a');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess('b', 'a'));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError(actualIsNull());
    charactersWithCaseInsensitiveComparisonStrategy.assertLessThan(someInfo(), null, 'a');
  }

  @Test
  public void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    charactersWithCaseInsensitiveComparisonStrategy.assertLessThan(someInfo(), 'A', 'b');
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      charactersWithCaseInsensitiveComparisonStrategy.assertLessThan(info, 'b', 'B');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess('b', 'B', caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      charactersWithCaseInsensitiveComparisonStrategy.assertLessThan(info, 'B', 'a');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess('B', 'a', caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
