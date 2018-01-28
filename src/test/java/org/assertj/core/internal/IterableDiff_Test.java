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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.IterableDiff.diff;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Class for testing <code>{@link IterableDiff}</code>
 *
 * @author Billy Yuan
 */
public class IterableDiff_Test {

  @Rule
  public ExpectedException thrown = none();

  private List<String> actual;
  private List<String> expected;
  private ComparisonStrategy comparisonStrategy;

  @Before
  public void setUp() {
    comparisonStrategy = StandardComparisonStrategy.instance();
  }

  @Test
  public void should_not_report_any_differences_between_two_identical_iterables() {
    // GIVEN
    actual = newArrayList("#", "$");
    expected = newArrayList("#", "$");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThatNoDiff(diff);
  }

  @Test
  public void should_not_report_any_differences_between_two_iterables_with_elements_in_a_different_order() {
    // GIVEN
    actual = newArrayList("#", "$");
    expected = newArrayList("$", "#");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThatNoDiff(diff);
  }

  @Test
  public void should_not_report_any_differences_between_two_iterables_with_duplicate_elements_in_a_different_order() {
    // GIVEN
    actual = newArrayList("#", "#", "$", "$");
    expected = newArrayList("$", "$", "#", "#");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThatNoDiff(diff);
  }

  @Test
  public void should_report_difference_between_two_different_iterables_without_duplicate_elements() {
    // GIVEN
    actual = newArrayList("A", "B", "C");
    expected = newArrayList("X", "Y", "Z");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).containsExactly("X", "Y", "Z");
    assertThat(diff.unexpected).containsExactly("A", "B", "C");
  }

  @Test
  public void should_report_difference_between_two_different_iterables_with_duplicate_elements() {
    // GIVEN
    actual = newArrayList("#", "#", "$");
    expected = newArrayList("$", "$", "#");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).containsExactly("$");
    assertThat(diff.unexpected).containsExactly("#");
  }

  @Test
  public void should_not_report_any_differences_between_two_case_sensitive_iterables_according_to_custom_comparison_strategy() {
    // GIVEN
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    actual = newArrayList("a", "b", "C", "D");
    expected = newArrayList("A", "B", "C", "D");
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThatNoDiff(diff);
  }

  @Test
  public void should_not_report_any_differences_between_two_same_iterables_with_custom_objects() {
    // GIVEN
    Foo foo1 = new Foo();
    Foo foo2 = new Foo();
    Foo foo3 = new Foo();
    List<Foo> actual = newArrayList(foo1, foo2, foo3);
    List<Foo> expected = newArrayList(foo1, foo2, foo3);
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThatNoDiff(diff);
  }

  @Test
  public void should_report_difference_between_two_iterables_with_duplicate_objects() {
    // GIVEN
    Foo foo1 = new Foo();
    Foo foo2 = new Foo();
    List<Foo> actual = newArrayList(foo1, foo1, foo2);
    List<Foo> expected = newArrayList(foo1, foo2, foo2);
    // WHEN
    IterableDiff diff = diff(actual, expected, comparisonStrategy);
    // THEN
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).containsExactly(foo2);
    assertThat(diff.unexpected).containsExactly(foo1);
  }

  private class Foo {
  }

  private static void assertThatNoDiff(IterableDiff diff) {
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing).isEmpty();
    assertThat(diff.unexpected).isEmpty();
  }

}
