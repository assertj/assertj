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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_containsCells_Test extends TableAssertBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsCell(1, 2, "");
  }

  @Test
  public void should_pass_if_actual_contains_cell() {
    assertThat(actual).containsCell(1, 3, "Millard Fillmore");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_cell() {
    try {
      assertThat(actual).containsCell(1, 4, "Millard Fillmore");
      fail("Assertion error expected.");
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\nExpecting row:<1> and column:<4> to have value:\n" +
                               "  <Millard Fillmore>\n" +
                               "but was:\n" +
                               "  <Franklin Pierce>\n" +
                               "in:\n" +
                               "  <{1={4=Franklin Pierce, 3=Millard Fillmore}, 2={5=Grover Cleveland}}>");
      return;
    }
  }

  @Test
  public void should_fail_if_row_is_null() {
    expectException(IllegalArgumentException.class, "The row to look for should not be null.");
    assertThat(actual).containsCell(null, 3, "Millard Fillmore");
  }

  @Test
  public void should_fail_if_column_is_null() {
    expectException(IllegalArgumentException.class, "The column to look for should not be null.");
    assertThat(actual).containsCell(1, null, "Millard Fillmore");
  }

  @Test
  public void should_fail_if_value_is_null() {
    expectException(IllegalArgumentException.class, "The value to look for should not be null.");
    assertThat(actual).containsCell(1, 3, null);
  }
}
