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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.LinkedHashMultimap;

public class MultimapAssert_hasSameEntriesAs_Test extends MultimapAssertBaseTest {

  private LinkedHashMultimap<String, String> other;

  @Before
  public void setup() {
    other = LinkedHashMultimap.create();
  }

  @Test
  public void should_pass_if_actual_has_the_same_entries_as_the_given_multimap() {
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    assertThat(actual).hasSameEntriesAs(other);
    assertThat(other).hasSameEntriesAs(actual);
  }

  @Test
  public void should_pass_with_multimaps_having_the_same_entries_with_different_but_compatible_generic_types() {
    LinkedHashMultimap<Object, Object> other = LinkedHashMultimap.create();
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    assertThat(other).hasSameEntriesAs(actual);
  }

  @Test
  public void should_pass_if_both_multimaps_are_empty() {
    actual.clear();
    assertThat(actual).hasSameEntriesAs(other);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).hasSameEntriesAs(other);
  }

  @Test
  public void should_fail_if_multimap_to_compare_actual_with_is_null() {
    expectException(IllegalArgumentException.class, "The multimap to compare actual with should not be null");
    assertThat(actual).hasSameEntriesAs(null);
  }

  @Test
  public void should_fail_if_actual_contains_entries_not_in_given_multimap() {
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    try {
      assertThat(actual).hasSameEntriesAs(other);
    } catch (AssertionError e) {
      // @format:off
      assertThat(e).hasMessage(format("%nExpecting:%n"                                   +
                               "  <{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>%n" +
                               "to contain only:%n" +
                               "  <{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose]}>%n" +
                               "but the following elements were unexpected:%n" +
                               "  <[Spurs=Tony Parker, Spurs=Tim Duncan, Spurs=Manu Ginobili]>%n"));
      // @format:on
      return;
    }
    fail("Assertion error expected");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_multimap_entries() {
    other.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    other.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
    other.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    other.putAll("Warriors", newArrayList("Stephen Curry", "Klay Thompson"));
    try {
      assertThat(actual).hasSameEntriesAs(other);
    } catch (AssertionError e) {
      // @format:off
      assertThat(e).hasMessage(format("%nExpecting:%n"                                   +
                               "  <{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili]}>%n" +
                               "to contain only:%n" +
                               "  <{Lakers=[Kobe Bryant, Magic Johnson, Kareem Abdul Jabbar], Bulls=[Michael Jordan, Scottie Pippen, Derrick Rose], Spurs=[Tony Parker, Tim Duncan, Manu Ginobili], Warriors=[Stephen Curry, Klay Thompson]}>%n" +
                               "but could not find the following elements:%n" +
                               "  <[Warriors=Stephen Curry, Warriors=Klay Thompson]>%n"));
      // @format:on
      return;
    }
    fail("Assertion error expected");
  }
}
