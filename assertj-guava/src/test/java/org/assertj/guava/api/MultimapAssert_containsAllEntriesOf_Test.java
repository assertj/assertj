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

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

class MultimapAssert_containsAllEntriesOf_Test extends MultimapAssertBaseTest {

  private final Multimap<String, String> other = LinkedHashMultimap.create();

  @Test
  void should_pass_if_actual_contains_exactly_all_entries_of_given_multimap() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // WHEN/THEN
    assertThat(actual).containsAllEntriesOf(other);
    assertThat(other).containsAllEntriesOf(actual);
  }

  @Test
  void should_pass_if_actual_contains_all_entries_of_given_multimap_and_additional_ones() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    // WHEN/THEN
    assertThat(actual).containsAllEntriesOf(other);
  }

  @Test
  void should_pass_with_different_multimaps_type_but_compatible_generic_types() {
    // GIVEN
    Multimap<Object, Object> other = HashMultimap.create();
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // WHEN/THEN
    assertThat(other).containsAllEntriesOf(actual);
  }

  @Test
  void should_pass_if_both_multimaps_are_empty() {
    // GIVEN
    actual.clear();
    // WHEN/THEN
    assertThat(actual).containsAllEntriesOf(other);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_multimap_to_compare_actual_with_is_null() {
    // GIVEN
    Multimap<String, String> other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The multimap to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_multimap_entries() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    other.putAll("Warriors", newArrayList("Stephen Curry", "Klay Thompson"));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    then(error).hasMessage(shouldContain(actual, other,
                                         list(entry("Warriors", "Stephen Curry"), entry("Warriors", "Klay Thompson"))).create());
  }

}
