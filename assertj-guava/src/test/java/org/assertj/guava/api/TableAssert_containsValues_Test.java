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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_containsValues_Test extends TableAssertBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues("a", "b", "c"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_contains_values() {
    assertThat(actual).containsValues("Franklin Pierce", "Millard Fillmore");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues(values));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be null.");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_empty() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be empty.");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    try {
      assertThat(actual).containsValues("James A. Garfield", "Andrew Johnson");
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%n" +
                                      "Expecting:%n" +
                                      "  {1={3=Millard Fillmore, 4=Franklin Pierce}, 2={5=Grover Cleveland}}%n" +
                                      "to contain values:%n" +
                                      "  [\"James A. Garfield\", \"Andrew Johnson\"]%n" +
                                      "but could not find:%n" +
                                      "  [\"Andrew Johnson\", \"James A. Garfield\"]"));
      return;
    }
    fail("Assertion error expected.");
  }

}
