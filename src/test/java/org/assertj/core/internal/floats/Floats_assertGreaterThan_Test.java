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
package org.assertj.core.internal.floats;

import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Floats#assertGreaterThan(AssertionInfo, Float, float)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Floats_assertGreaterThan_Test extends FloatsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    floats.assertGreaterThan(someInfo(), null, 8f);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    floats.assertGreaterThan(someInfo(), 8f, 6f);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other() {
    AssertionInfo info = someInfo();
    try {
      floats.assertGreaterThan(info, 6f, 6f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(6f, 6f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      floats.assertGreaterThan(info, 6f, 8f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(6f, 8f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), -8f, 6f);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      floatsWithAbsValueComparisonStrategy.assertGreaterThan(info, -6f, 6f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(-6f, 6f, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      floatsWithAbsValueComparisonStrategy.assertGreaterThan(info, -6f, 8f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(-6f, 8f, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
