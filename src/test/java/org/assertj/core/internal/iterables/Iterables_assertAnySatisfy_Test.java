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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

public class Iterables_assertAnySatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda", "Obiwan");

  @Test
  public void should_pass_when_one_element_satisfies_the_single_assertion_requirement() {
    iterables.<String> assertAnySatisfy(someInfo(), actual, s -> assertThat(s).hasSize(6));
  }

  @Test
  public void should_pass_when_one_element_satisfies_all_the_assertion_requirements() {
    iterables.<String> assertAnySatisfy(someInfo(), actual, s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    });
  }

  @Test
  public void should_pass_when_several_elements_satisfy_all_the_assertion_requirements() {
    iterables.<String> assertAnySatisfy(someInfo(), actual, s -> {
      assertThat(s).hasSize(4);
      assertThat(s).contains("L");
    });
  }

  @Test
  public void should_fail_if_no_elements_satisfy_the_assertions_requirements() {
    try {
      iterables.<String> assertAnySatisfy(someInfo(), actual, s -> {
        assertThat(s).hasSize(4);
        assertThat(s).contains("W");
      });
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsShouldSatisfyAny(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_the_iterable_under_test_is_empty_whatever_the_assertions_requirements_are() {
    actual.clear();
    try {
      iterables.<String> assertAnySatisfy(someInfo(), actual, $ -> assertThat(true).isTrue());
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsShouldSatisfyAny(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_consumer_is_null() {
    thrown.expectNullPointerException("The Consumer<T> expressing the assertions requirements must not be null");
    assertThat(actual).anySatisfy(null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    actual = null;
    assertThat(actual).anySatisfy(null);
  }
}