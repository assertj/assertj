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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

public class Iterables_assertNoneSatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

  @Test
  public void should_pass_when_no_elements_satisfy_the_given_single_restriction() {
    // GIVEN
    Consumer<String> restriction = name -> assertThat(name).hasSize(5);
    // THEN
    iterables.assertNoneSatisfy(someInfo(), actual, restriction);
  }

  @Test
  public void should_pass_when_no_elements_satisfy_the_given_restrictions() {
    // GIVEN
    Consumer<String> restrictions = name -> {
      assertThat(name).hasSize(5);
      assertThat(name).contains("V");
    };
    // THEN
    iterables.assertNoneSatisfy(someInfo(), actual, restrictions);
  }

  @Test
  public void should_pass_for_empty_whatever_the_given_restrictions_are() {
    // GIVEN
    Consumer<String> restriction = name -> assertThat(name).hasSize(5);
    actual.clear();
    // THEN
    iterables.assertNoneSatisfy(someInfo(), actual, restriction);
  }

  @Test
  public void should_fail_when_one_elements_satisfy_the_given_restrictions() {
    // GIVEN
    Consumer<String> restrictions = name -> assertThat(name).startsWith("Y");
    // WHEN
    Throwable assertionError = catchThrowable(() -> iterables.assertNoneSatisfy(someInfo(), actual, restrictions));
    // THEN
    verify(failures).failure(info, noElementsShouldSatisfy(actual, "Yoda"));
    assertThat(assertionError).isNotNull();
  }

  @Test
  public void should_throw_error_if_consumer_restrictions_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertNoneSatisfy(someInfo(), actual, null))
                                    .withMessage("The Consumer<T> expressing the restrictions must not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
      List<String> nullActual = null;
      iterables.assertNoneSatisfy(someInfo(), nullActual, name -> assertThat(name).startsWith("Y"));
    }).withMessage(actualIsNull());
  }

}
