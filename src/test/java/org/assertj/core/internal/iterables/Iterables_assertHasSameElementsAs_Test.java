/*
 * Created on August 21, 2014
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
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldHaveSameElementsAs.*;
import static org.assertj.core.test.ErrorMessages.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.*;

import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Iterables#assertHasSameElementsAs(org.assertj.core.api.AssertionInfo, Iterable, Iterable)}</code>
 * .
 *
 * @author Adam Ruka
 */
public class Iterables_assertHasSameElementsAs_Test extends IterablesBaseTest {
  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertHasSameElementsAs(someInfo(), null, newArrayList("Yoda"));
  }

  @Test
  public void should_fail_if_expected_is_null() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    iterables.assertHasSameElementsAs(someInfo(), newArrayList("Yoda"), null);
  }

  @Test
  public void should_pass_if_has_elements_in_different_order() {
    iterables.assertHasSameElementsAs(someInfo(), newArrayList("a", "b"), newArrayList("b", "a"));
  }

  @Test
  public void should_pass_if_has_more_same_elements() {
    iterables.assertHasSameElementsAs(someInfo(), newArrayList("a", "b", "a"), newArrayList("b", "a"));
  }

  @Test
  public void should_fail_if_has_different_elements() {
    Iterable<String> actual = newArrayList("a", "b", "a");
    Iterable<String> expected = newArrayList("b", "a", "c");
    try {
      iterables.assertHasSameElementsAs(someInfo(), actual, expected);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(shouldHaveSameElementsAs(actual, expected, StandardComparisonStrategy.instance()).create());
    }
  }
}
