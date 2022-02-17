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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

class MultimapAssert_hasSameEntriesAs_Test extends MultimapAssertBaseTest {

  private final Multimap<String, String> other = LinkedHashMultimap.create();

  @Test
  void should_pass_if_actual_has_the_same_entries_as_the_given_multimap() {
    // GIVEN
    other.putAll("Lakers", list("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", list("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", list("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // THEN
    assertThat(actual).hasSameEntriesAs(other);
    assertThat(other).hasSameEntriesAs(actual);
  }

  @Test
  void should_pass_with_multimaps_having_the_same_entries_with_different_but_compatible_generic_types() {
    // GIVEN
    Multimap<Object, Object> other = LinkedHashMultimap.create();
    other.putAll("Lakers", list("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", list("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", list("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // THEN
    assertThat(other).hasSameEntriesAs(actual);
  }

  @Test
  void should_pass_if_both_multimaps_are_empty() {
    // GIVEN
    actual.clear();
    // THEN
    assertThat(actual).hasSameEntriesAs(other);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasSameEntriesAs(other));
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_multimap_to_compare_actual_with_is_null() {
    // GIVEN
    Multimap<String, String> other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasSameEntriesAs(other));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The multimap to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_contains_entries_not_in_given_multimap() {
    // GIVEN
    other.putAll("Lakers", list("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", list("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSameEntriesAs(other));
    // THEN
    then(error).hasMessage(shouldContainOnly(actual, other, null,
                                             list(entry("Spurs", "Tony Parker"), entry("Spurs", "Tim Duncan"),
                                                  entry("Spurs", "Manu Ginobili"))).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_multimap_entries() {
    // GIVEN
    other.putAll("Lakers", list("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", list("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", list("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    other.putAll("Warriors", list("Stephen Curry", "Klay Thompson"));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSameEntriesAs(other));
    // THEN
    then(error).hasMessage(shouldContainOnly(actual, other,
                                             list(entry("Warriors", "Stephen Curry"), entry("Warriors", "Klay Thompson")),
                                             null).create());
  }

}
