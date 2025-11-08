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
package org.assertj.tests.guava.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.tests.guava.testkit.AssertionErrors.expectAssertionError;

import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.guava.api.TableAssert;
import org.assertj.guava.api.TableIntegerAssert;
import org.junit.jupiter.api.Test;

/**
 * @author Maciej Kucharczyk
 */
class TableAssert_rowCount_Test extends TableAssertBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).rowCount());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void integer_assert_should_return_to_source_table() {
    // GIVEN
    TableAssert<Integer, Integer, String> tableAssertion = assertThat(actual);
    TableIntegerAssert<Integer, Integer, String> rowCountAssertion = tableAssertion.rowCount();
    // WHEN
    TableAssert<Integer, Integer, String> returnedAssertion = rowCountAssertion.returnToTable();
    // THEN
    then(returnedAssertion).isSameAs(tableAssertion);
  }

  @Test
  void should_hand_over_row_count_as_integer_assert_actual() {
    // GIVEN
    TableAssert<Integer, Integer, String> assertion = assertThat(actual);
    // WHEN
    AbstractIntegerAssert<?> returnedAssertion = assertion.rowCount();
    // THEN
    then(returnedAssertion.actual()).isEqualTo(2);
    then(returnedAssertion.info.descriptionText()).contains("checking row count");
  }

  @Test
  void javadoc_example_should_work() {
    assertThat(actual).rowCount()
                      .isGreaterThan(1)
                      .isLessThan(5)
                      .isEqualTo(2);
  }

}
