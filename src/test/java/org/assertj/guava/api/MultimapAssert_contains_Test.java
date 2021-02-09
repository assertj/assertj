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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.guava.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

public class MultimapAssert_contains_Test extends MultimapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_entries() {
    assertThat(actual).contains(entry("Bulls", "Derrick Rose"));
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains(entry("Lakers", "Kobe Bryant")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_null() {
    // GIVEN
    MapEntry<String, String>[] entries = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains(entries));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The entries to look for should not be null");
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_empty() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The entries to look for should not be empty");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_entries() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains(entry("Lakers", "Kobe Bryant"),
                                                                           entry("Spurs", "Derrick Rose")));
    // THEN
    // @format:off
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format("%nExpecting LinkedListMultimap:%n" +
                                     "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}%n" +
                                     "to contain:%n" +
                                     "  [MapEntry[key=\"Lakers\", value=\"Kobe Bryant\"],%n" +
                                     "    MapEntry[key=\"Spurs\", value=\"Derrick Rose\"]]%n" +
                                     "but could not find the following element(s):%n" +
                                     "  [MapEntry[key=\"Spurs\", value=\"Derrick Rose\"]]%n"));
      // @format:on
  }
}
