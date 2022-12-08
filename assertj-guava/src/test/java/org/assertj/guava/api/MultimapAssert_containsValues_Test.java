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
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class MultimapAssert_containsValues_Test extends MultimapAssertBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values() {
    assertThat(actual).containsValues("Manu Ginobili");
    assertThat(actual).containsValues("Magic Johnson", "Derrick Rose");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues("Manu Ginobili", "Derrick Rose"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_values_to_look_for_are_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues(values));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be null");
  }

  @Test
  void should_fail_if_values_to_look_for_are_empty() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsValues());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The values to look for should not be empty");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values() {
    try {
      assertThat(actual).containsValues("Magic Johnson", "Lebron James");
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("%nExpecting:%n"
                                      + "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}%n"
                                      + "to contain values:%n"
                                      + "  [\"Magic Johnson\", \"Lebron James\"]%n"
                                      + "but could not find:%n"
                                      + "  [\"Lebron James\"]"));
      return;
    }
    fail("Assertion error expected");
  }

  @Test
  void should_fail_if_actual_does_not_contain_the_given_value() {
    try {
      assertThat(actual).containsValues("Lebron James");
    } catch (AssertionError e) {
      // error message shows that we were looking for a unique value (not many)
      assertThat(e).hasMessage(format("%nExpecting:%n"
                                      + "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}%n"
                                      + "to contain value:%n"
                                      + "  \"Lebron James\""));
      return;
    }
    fail("Assertion error expected");
  }

}
