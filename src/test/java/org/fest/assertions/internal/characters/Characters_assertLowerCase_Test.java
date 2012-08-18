/*
 * Created on Jan 25, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.characters;

import static org.fest.assertions.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Characters;
import org.fest.assertions.internal.CharactersBaseTest;

/**
 * Tests for <code>{@link Characters#assertLowerCase(AssertionInfo, Character)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Characters_assertLowerCase_Test extends CharactersBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    characters.assertLowerCase(someInfo(), null);
  }

  @Test
  public void should_pass_if_actual_is_lowercase() {
    characters.assertLowerCase(someInfo(), 'a');
  }

  @Test
  public void should_fail_if_actual_is_not_lowercase() {
    AssertionInfo info = someInfo();
    try {
      characters.assertLowerCase(info, 'A');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLowerCase('A'));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(someInfo(), null);
  }

  @Test
  public void should_pass_if_actual_is_lowercase_whatever_custom_comparison_strategy_is() {
    charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(someInfo(), 'a');
  }

  @Test
  public void should_fail_if_actual_is_not_lowercase_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(info, 'A');
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLowerCase('A'));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
