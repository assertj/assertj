/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.Configuration.MAX_INDICES_FOR_PRINTING;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactlyWithIndexes;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IndexedDiff;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

class ShouldContainExactly_create_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);

  @Test
  void should_display_full_expected_and_actual_sets_with_index_when_order_does_not_match() {
    // GIVEN
    List<String> actual = list("Yoda", "Han", "Luke", "Anakin");
    List<String> expected = list("Yoda", "Luke", "Han", "Anakin");
    List<IndexedDiff> indexDifferences = list(new IndexedDiff(actual.get(1), expected.get(1), 1),
                                              new IndexedDiff(actual.get(2), expected.get(2), 2));
    ErrorMessageFactory factory = shouldContainExactlyWithIndexes(actual, expected, indexDifferences,
                                                                  StandardComparisonStrategy.instance());

    // WHEN
    final String message = factory.create(new TextDescription("Test"));

    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\", \"Luke\", \"Anakin\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Yoda\", \"Luke\", \"Han\", \"Anakin\"]%n" +
                                   "but there were differences at these indexes:%n" +
                                   "  - element at index 1: expected \"Luke\" but was \"Han\"%n" +
                                   "  - element at index 2: expected \"Han\" but was \"Luke\"%n"));
  }

  @Test
  void should_display_only_configured_max_amount_of_indices() {
    // GIVEN
    List<Integer> expected = IntStream.rangeClosed(0, MAX_INDICES_FOR_PRINTING)
                                      .boxed()
                                      .collect(Collectors.toList());
    List<Integer> actual = IntStream.rangeClosed(0, MAX_INDICES_FOR_PRINTING)
                                    .boxed()
                                    .sorted(Comparator.reverseOrder())
                                    .collect(Collectors.toList());
    List<IndexedDiff> indexDifferences = new ArrayList<>();
    for (int i = 0; i < actual.size(); i++) {
      indexDifferences.add(new IndexedDiff(actual.get(i), expected.get(i), i));
    }

    ErrorMessageFactory factory = shouldContainExactlyWithIndexes(actual, expected, indexDifferences,
                                                                  StandardComparisonStrategy.instance());

    // WHEN
    final String message = factory.create(new TextDescription("Test"));

    // THEN
    then(message).contains(format("only showing the first %d mismatches", MAX_INDICES_FOR_PRINTING));
  }

  @Test
  void should_display_full_expected_and_actual_sets_with_missing_and_unexpected_elements() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda", "H%an", "Luk%e", "Anakin"),
                                                       list("Yoda", "H%an", "Anak%in", "Anakin"),
                                                       list("Anak%in"), list("Luk%e"));

    // WHEN
    final String message = factory.create(new TextDescription("Test"));

    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"H%%an\", \"Luk%%e\", \"Anakin\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Yoda\", \"H%%an\", \"Anak%%in\", \"Anakin\"]%n" +
                                   "but some elements were not found:%n" +
                                   "  [\"Anak%%in\"]%n" +
                                   "and others were not expected:%n" +
                                   "  [\"Luk%%e\"]%n"));
  }

  @Test
  void should_display_missing_and_unexpected_elements() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda", "Han"), list("Luke", "Yoda"), newLinkedHashSet("Luke"),
                                                       newLinkedHashSet("Han"));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but some elements were not found:%n" +
                                   "  [\"Luke\"]%n" +
                                   "and others were not expected:%n" +
                                   "  [\"Han\"]%n"));
  }

  @Test
  void should_not_display_missing_elements_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda", "Han"), list("Yoda"), list(), list("Han"));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Yoda\"]%n" +
                                   "but some elements were not expected:%n" +
                                   "  [\"Han\"]%n"));
  }

  @Test
  void should_not_display_unexpected_elements_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda"), list("Luke", "Yoda"), newLinkedHashSet("Luke"), emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but could not find the following elements:%n" +
                                   "  [\"Luke\"]%n"));
  }

  @Test
  void should_display_first_wrong_element_when_only_elements_order_differs() {
    // GIVEN
    ErrorMessageFactory factory = elementsDifferAtIndex("Luke", "H%an", 1);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Actual and expected have the same elements but not in the same order, at index 1 actual element was:%n"
                                   + "  \"Luke\"%n"
                                   + "whereas expected element was:%n"
                                   + "  \"H%%an\"%n"));
  }

  @DisabledOnOs(OS.WINDOWS)
  @Test
  void should_escape_percentage_sign_from_elements_listed_in_index_differences() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactlyWithIndexes(list("Yoda"), list("%"),
                                                                  list(new IndexedDiff("Yoda", "%", 1)));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"%%\"]%n" +
                                   "but there were differences at these indexes:%n" +
                                   "  - element at index 1: expected \"%%\" but was \"Yoda\"\n"));
  }

  // with custom comparison strategy

  @Test
  void should_display_missing_and_unexpected_elements_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda", "Han"),
                                                       list("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"),
                                                       newLinkedHashSet("Han"),
                                                       CASE_INSENSITIVE_COMPARISON_STRATEGY);

    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but some elements were not found:%n" +
                                   "  [\"Luke\"]%n" +
                                   "and others were not expected:%n" +
                                   "  [\"Han\"]%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_display_first_wrong_element_when_only_elements_order_differs_according_to_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = elementsDifferAtIndex("Luke", "Han", 1, CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Actual and expected have the same elements but not in the same order, at index 1 actual element was:%n"
                                   + "  \"Luke\"%nwhereas expected element was:%n"
                                   + "  \"Han\"%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_not_display_unexpected_elements_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda"),
                                                       list("Luke", "Yoda"),
                                                       newLinkedHashSet("Luke"),
                                                       emptySet(),
                                                       CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Luke\", \"Yoda\"]%n" +
                                   "but could not find the following elements:%n" +
                                   "  [\"Luke\"]%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  void should_not_display_missing_elements_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainExactly(list("Yoda", "Han"), list("Yoda"),
                                                       list(), list("Han"),
                                                       CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  [\"Yoda\", \"Han\"]%n" +
                                   "to contain exactly (and in same order):%n" +
                                   "  [\"Yoda\"]%n" +
                                   "but some elements were not expected:%n" +
                                   "  [\"Han\"]%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

}
