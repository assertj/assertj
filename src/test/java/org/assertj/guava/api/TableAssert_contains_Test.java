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
 * Copyright 2012-2013 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;


import org.assertj.core.api.Assertions;

import org.junit.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_contains_Test extends TableAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_rows() {
    assertThat(actual).containsRows(1, 2);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsRows(1, 2);
  }

  @Test
  public void should_fail_if_rows_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The rows to look for should not be null");
    assertThat(actual).containsRows((Integer[]) null);
  }

  @Test
  public void should_fail_if_rows_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The rows to look for should not be empty");
    assertThat(actual).containsRows();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_rows() {
    try {
      assertThat(actual).containsRows(3, 4);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("expecting:\n" +
                               "<{1={4=Franklin Pierce, 3=Millard Fillmore}, 2={5=Grover Cleveland}}>\n" +
                               " to contain rows:\n" +
                               "<[3, 4]>\n" +
                               " but could not find:\n" +
                               "<[3, 4]>");
      return;
    }
    fail("Assertion error expected.");
  }

  @Test
  public void should_pass_if_actual_contains_columns() {
    assertThat(actual).containsColumns(3, 4);
  }

  @Test
  public void should_fail_if_columns_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The columns to look for should not be null");
    assertThat(actual).containsColumns((Integer[]) null);
  }

  @Test
  public void should_fail_if_columns_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The columns to look for should not be empty");
    assertThat(actual).containsColumns();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_columns() {
    try {
      assertThat(actual).containsColumns(9, 6);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("expecting:\n" +
                               "<{1={4=Franklin Pierce, 3=Millard Fillmore}, 2={5=Grover Cleveland}}>\n" +
                               " to contain columns:\n" +
                               "<[9, 6]>\n" +
                               " but could not find:\n" +
                               "<[6, 9]>");
      return;
    }
    fail("Assertion error expected.");
  }

  @Test
  public void should_pass_if_actual_contains_values() {
    assertThat(actual).containsValues("Franklin Pierce", "Millard Fillmore");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The values to look for should not be null");
    assertThat(actual).containsValues((String[]) null);
  }

  @Test
  public void should_fail_if_values_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The values to look for should not be empty");
    assertThat(actual).containsValues();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    try {
      assertThat(actual).containsValues("James A. Garfield", "Andrew Johnson");
    } catch (AssertionError e) {
      assertThat(e).hasMessage("expecting:\n" +
                               "<{1={4=Franklin Pierce, 3=Millard Fillmore}, 2={5=Grover Cleveland}}>\n" +
                               " to contain values:\n" +
                               "<[\"James A. Garfield\", \"Andrew Johnson\"]>\n" +
                               " but could not find:\n" +
                               "<[\"Andrew Johnson\", \"James A. Garfield\"]>");
      return;
    }
    fail("Assertion error expected.");
  }
}
