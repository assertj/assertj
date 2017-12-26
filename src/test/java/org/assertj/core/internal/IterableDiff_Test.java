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
 * Copyright 2012-2017 the original author or authors.
 */

package org.assertj.core.internal;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.test.ExpectedException.*;
import static org.assertj.core.util.Lists.*;

/**
 * Class for testing <code>{@link IterableDiff}</code>
 *
 * @author Billy Yuan
 */

public class IterableDiff_Test {

  @Rule
  public ExpectedException thrown = none();

  private List<String> actual;
  private ComparisonStrategy comparisonStrategy;
  private List<String> expected;

  @Before
  public void setUp() {
    comparisonStrategy = StandardComparisonStrategy.instance();
  }

  @Test
  public void no_difference_found_between_two_same_iterables() {
    actual = newArrayList("#", "$");
    expected = newArrayList("#", "$");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing.isEmpty()).isTrue();
    assertThat(diff.unexpected.isEmpty()).isTrue();
  }

  @Test
  public void no_difference_found_between_two_same_iterables_without_same_order() {
    actual = newArrayList("#", "$");
    expected = newArrayList("$", "#");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing.isEmpty()).isTrue();
    assertThat(diff.unexpected.isEmpty()).isTrue();
  }

  @Test
  public void no_difference_found_between_two_same_iterables_with_duplicate_elements() {
    actual = newArrayList("#", "#", "$", "$");
    expected = newArrayList("$", "$", "#", "#");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing.isEmpty()).isTrue();
    assertThat(diff.unexpected.isEmpty()).isTrue();
  }

  @Test
  public void difference_found_between_two_different_iterables() {
    actual = newArrayList("A", "B", "C");
    expected = newArrayList("X", "Y", "Z");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).isEqualTo(newArrayList("X", "Y", "Z"));
    assertThat(diff.unexpected).isEqualTo(newArrayList("A", "B", "C"));
  }

  @Test
  public void difference_found_between_two_different_iterables_with_duplicate_elements() {
    actual = newArrayList("#", "#", "$");
    expected = newArrayList("$", "$", "#");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).isEqualTo(newArrayList("$"));
    assertThat(diff.unexpected).isEqualTo(newArrayList("#"));
  }

  @Test
  public void no_difference_found_between_two_case_sensitive_iterables_according_to_custom_comparison_strategy() {
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    actual = newArrayList("a", "b", "C", "D");
    expected = newArrayList("A", "B", "C", "D");
    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing.isEmpty()).isTrue();
    assertThat(diff.unexpected.isEmpty()).isTrue();
  }

  @Test
  public void no_difference_found_between_two_same_iterables_with_custom_objects() {
    Foo foo1 = new Foo();
    Foo foo2 = new Foo();
    Foo foo3 = new Foo();

    List<Foo> actual = newArrayList(foo1, foo2, foo3);
    List<Foo> expected = newArrayList(foo1, foo2, foo3);

    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isFalse();
    assertThat(diff.missing.isEmpty()).isTrue();
    assertThat(diff.unexpected.isEmpty()).isTrue();
  }

  @Test
  public void difference_found_between_two_iterables_with_duplicate_objects() {
    Foo foo1 = new Foo();
    Foo foo2 = new Foo();

    List<Foo> actual = newArrayList(foo1, foo1, foo2);
    List<Foo> expected = newArrayList(foo1, foo2, foo2);

    IterableDiff diff = IterableDiff.diff(actual, expected, comparisonStrategy);
    assertThat(diff.differencesFound()).isTrue();
    assertThat(diff.missing).isEqualTo(newArrayList(foo2));
    assertThat(diff.unexpected).isEqualTo(newArrayList(foo1));
  }

  @SuppressWarnings("unused")
  private class Foo {
  }
}
