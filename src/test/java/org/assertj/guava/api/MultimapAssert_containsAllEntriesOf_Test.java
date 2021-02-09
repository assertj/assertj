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

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class MultimapAssert_containsAllEntriesOf_Test extends MultimapAssertBaseTest {

  private Multimap<String, String> other;

  @BeforeEach
  public void setup() {
    other = LinkedHashMultimap.create();
  }

  @Test
  public void should_pass_if_actual_contains_exactly_all_entries_of_given_multimap() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // THEN
    assertThat(actual).containsAllEntriesOf(other);
    assertThat(other).containsAllEntriesOf(actual);
  }

  @Test
  public void should_pass_if_actual_contains_all_entries_of_given_multimap_and_additional_ones() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    // THEN
    assertThat(actual).containsAllEntriesOf(other);
  }

  @Test
  public void should_pass_with_different_multimaps_type_but_compatible_generic_types() {
    // GIVEN
    Multimap<Object, Object> other = HashMultimap.create();
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    // THEN
    assertThat(other).containsAllEntriesOf(actual);
  }

  @Test
  public void should_pass_if_both_multimaps_are_empty() {
    // GIVEN
    actual.clear();
    // THEN
    assertThat(actual).containsAllEntriesOf(other);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_multimap_to_compare_actual_with_is_null() {
    // GIVEN
    Multimap<? extends String, ? extends String> other = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The multimap to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_multimap_entries() {
    // GIVEN
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    other.putAll("Warriors", newArrayList("Stephen Curry", "Klay Thompson"));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAllEntriesOf(other));
    // THEN
    // @format:off
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format("%nExpecting LinkedListMultimap:%n"                                   +
                                            "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}%n" +
                                            "to contain:%n" +
                                            "  {Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili], Warriors=[Stephen Curry, Klay Thompson]}%n" +
                                            "but could not find the following element(s):%n" +
                                            "  [Warriors=Stephen Curry, Warriors=Klay Thompson]%n"));
    // @format:on
  }
}
