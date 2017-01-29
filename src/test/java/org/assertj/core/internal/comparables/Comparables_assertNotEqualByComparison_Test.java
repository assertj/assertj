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
package org.assertj.core.internal.comparables;

import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparablesBaseTest;
import org.assertj.core.test.Person;
import org.assertj.core.test.PersonCaseInsensitiveNameComparator;
import org.junit.Test;


/**
 * Tests for <code>{@link Comparables#assertNotEqualByComparison(AssertionInfo, Comparable, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Comparables_assertNotEqualByComparison_Test extends ComparablesBaseTest {

  @Override
  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return new PersonCaseInsensitiveNameComparator();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    comparables.assertNotEqualByComparison(someInfo(), null, 8);
  }

  @Test
  public void should_pass_if_objects_are_not_equal() {
    Person a = spy(new Person("Han"));
    Person o = new Person("Yoda");
    comparables.assertNotEqualByComparison(someInfo(), a, o);
    verify(a).compareTo(o);
  }

  @Test
  public void should_fail_if_objects_are_equal() {
    AssertionInfo info = someInfo();
    try {
      comparables.assertNotEqualByComparison(info, "Yoda", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual("Yoda", "Yoda"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    comparablesWithCustomComparisonStrategy.assertNotEqualByComparison(someInfo(), null, new Person("Yoda"));
  }

  @Test
  public void should_pass_if_objects_are_not_equal_whatever_custom_comparison_strategy_is() {
    Person actual = spy(new Person("YODA"));
    Person other = new Person("Yoda");
    comparablesWithCustomComparisonStrategy.assertNotEqualByComparison(someInfo(), actual, other);
    verify(actual).compareTo(other);
  }

  @Test
  public void should_fail_if_objects_are_equal_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      comparablesWithCustomComparisonStrategy.assertNotEqualByComparison(info, new Person("Yoda"), new Person("Yoda"));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(new Person("Yoda"), new Person("Yoda")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
