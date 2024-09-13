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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.guava.api;

import org.assertj.guava.api.TableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.TableShouldHaveRowCountLessThan.tableShouldHaveRowCountLessThan;

/**
 * @author Maciej Kucharczyk
 */
class TableAssert_hasRowCountLessThan_Test extends TableAssertBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasRowCountLessThan(10));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class)
                   .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_negative() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasRowCountLessThan(-1));
    // THEN
    then(throwable).isInstanceOf(IllegalArgumentException.class)
                   .hasMessage("The expected size should not be negative.");
  }

  @Test
  void should_pass_if_rows_of_actual_is_less_than_boundary() {
    // WHEN/THEN
    assertThat(actual).hasRowCountLessThan(3);
  }

  @Test
  void should_fail_if_rows_of_actual_is_equal_to_boundary() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasRowCountLessThan(2));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class)
                   .hasMessage(tableShouldHaveRowCountLessThan(actual, 2, 2).create());
  }

  @Test
  void should_fail_if_rows_of_actual_is_greater_than_boundary() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).hasRowCountLessThan(1));
    // THEN
    then(throwable).isInstanceOf(AssertionError.class)
                   .hasMessage(tableShouldHaveRowCountLessThan(actual, 2, 1).create());
  }

  @Test
  void should_return_this() {
    // GIVEN
    TableAssert<Integer, Integer, String> assertion = assertThat(actual);
    // WHEN
    TableAssert<Integer, Integer, String> returnedAssertion = assertion.hasRowCountLessThan(10);
    // THEN
    then(returnedAssertion).isSameAs(assertion);
  }

}
