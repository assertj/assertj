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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.guava.api;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

class MultimapAssert_contains_Test extends MultimapAssertBaseTest {

  @Test
  void should_pass_if_actual_contains_given_entries() {
    assertThat(actual).contains(entry("Bulls", "Derrick Rose"));
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).contains(entry("Lakers", "Kobe Bryant")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_entries_to_look_for_are_null() {
    // GIVEN
    MapEntry<String, String>[] entries = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).contains(entries));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                   .hasMessage("The entries to look for should not be null");
  }

  @Test
  void should_fail_if_entries_to_look_for_are_empty() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).contains());
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The entries to look for should not be empty");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_entries() {
    // GIVEN
    MapEntry<String, String>[] entries = array(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Derrick Rose"));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).contains(entries));
    // THEN
    then(error).hasMessage(shouldContain(actual, entries, singleton(entry("Spurs", "Derrick Rose"))).create());
  }

}
