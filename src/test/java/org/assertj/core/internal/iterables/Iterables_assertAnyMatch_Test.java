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
package org.assertj.core.internal.iterables;

import static org.assertj.core.error.AnyElementShouldMatch.anyElementShouldMatch;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.Test;

public class Iterables_assertAnyMatch_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_an_element_satisfies_predicate() {
    List<String> actual = newArrayList("123", "1234", "12345");
    iterables.assertAnyMatch(someInfo(), actual, s -> s.length() >= 5, PredicateDescription.GIVEN);
  }

  @Test
  public void should_fail_if_predicate_is_not_met_by_any_elements() {
    List<String> actual = newArrayList("Luke", "Leia", "Yoda");
    Predicate<String> startsWithM = s -> s.startsWith("M");
    try {
      iterables.assertAnyMatch(info, actual, startsWithM, PredicateDescription.GIVEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, anyElementShouldMatch(actual, PredicateDescription.GIVEN));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_with_custom_description_if_predicate_is_met_by_no_element() {
    List<String> actual = newArrayList("Luke", "Leia", "Yoda");
    Predicate<String> startsWithM = s -> s.startsWith("M");
    try {
      iterables.assertAnyMatch(info, actual, startsWithM, new PredicateDescription("custom"));
    } catch (AssertionError e) {
      verify(failures).failure(info, anyElementShouldMatch(actual, new PredicateDescription("custom")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    actual = null;
    iterables.assertAnyMatch(someInfo(), actual, String::isEmpty, PredicateDescription.GIVEN);
  }

  @Test
  public void should_throw_error_if_predicate_is_null() {
    thrown.expectNullPointerException("The predicate to evaluate should not be null");
    iterables.assertAnyMatch(someInfo(), actual, null, PredicateDescription.GIVEN);
  }

}
