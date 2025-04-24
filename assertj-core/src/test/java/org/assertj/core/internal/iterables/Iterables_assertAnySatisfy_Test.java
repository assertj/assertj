/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

class Iterables_assertAnySatisfy_Test extends IterablesBaseTest {

  private final List<String> actual = newArrayList("Luke", "Leia", "Yoda", "Obiwan");

  @Test
  void must_not_check_all_elements() {
    // GIVEN
    assertThat(actual).hasSizeGreaterThan(2); // This test requires an iterable with size > 2
    Consumer<String> consumer = mock(Consumer.class);
    // first element does not match -> assertion error, 2nd element does match -> doNothing()
    doThrow(new AssertionError("some error message")).doNothing().when(consumer).accept(anyString());
    // WHEN
    iterables.assertAnySatisfy(someInfo(), actual, consumer);
    // THEN
    // make sure that we only evaluated 2 out of 4 elements
    verify(consumer, times(2)).accept(anyString());
  }

  @Test
  void should_pass_when_one_element_satisfies_the_single_assertion_requirement() {
    iterables.assertAnySatisfy(someInfo(), actual, s -> assertThat(s).hasSize(6));
  }

  @Test
  void should_pass_when_one_element_satisfies_all_the_assertion_requirements() {
    iterables.assertAnySatisfy(someInfo(), actual, s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    });
  }

  @Test
  void should_pass_when_several_elements_satisfy_all_the_assertion_requirements() {
    iterables.assertAnySatisfy(someInfo(), actual, s -> {
      assertThat(s).hasSize(4);
      assertThat(s).contains("L");
    });
  }

  @Test
  void should_fail_if_no_elements_satisfy_the_assertions_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertAnySatisfy(someInfo(), actual, s -> {
      assertThat(s).hasSize(4);
      assertThat(s).contains("W");
    }));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(error).hasMessageContainingAll(format("%n" +
                                               "Expecting actual:%n" +
                                               "  \"Luke\"%n" +
                                               "to contain:%n" +
                                               "  \"W\" "),
                                        format("%n" +
                                               "Expecting actual:%n" +
                                               "  \"Leia\"%n" +
                                               "to contain:%n" +
                                               "  \"W\" "),
                                        format("%n" +
                                               "Expecting actual:%n" +
                                               "  \"Yoda\"%n" +
                                               "to contain:%n" +
                                               "  \"W\" "),
                                        format("%n" +
                                               "Expected size: 4 but was: 6 in:%n"
                                               +
                                               "\"Obiwan\""));
  }

  @Test
  void should_fail_if_the_iterable_under_test_is_empty_whatever_the_assertions_requirements_are() {
    actual.clear();

    Throwable error = catchThrowable(() -> iterables.assertAnySatisfy(someInfo(), actual, $ -> assertThat(true).isTrue()));

    assertThat(error).isInstanceOf(AssertionError.class);
    List<UnsatisfiedRequirement> errors = emptyList();
    verify(failures).failure(info, elementsShouldSatisfyAny(actual, errors, someInfo()));
  }

  @Test
  void should_fail_if_consumer_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).anySatisfy(null))
                                    .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertAnySatisfy(someInfo(), null, $ -> {}));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }
}
