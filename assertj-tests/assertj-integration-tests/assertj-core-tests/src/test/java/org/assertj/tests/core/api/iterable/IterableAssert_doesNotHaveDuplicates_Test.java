/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.iterable;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_doesNotHaveDuplicates_Test {

  private static final int GENERATED_OBJECTS_NUMBER = 50000;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotHaveDuplicates());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_does_not_have_duplicates() {
    assertThat(List.of("Luke", "Yoda", "Leia")).doesNotHaveDuplicates();
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat(List.of()).doesNotHaveDuplicates();
  }

  @Test
  void should_fail_if_actual_contains_duplicates() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda", "Leia", "Luke", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotHaveDuplicates());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveDuplicates(actual, newLinkedHashSet("Luke", "Yoda")).create());
  }

  @Test
  void should_fail_if_actual_contains_duplicates_array() {
    // GIVEN
    List<String[]> actual = List.of(array("Luke", "Yoda"), array("Luke", "Yoda"));
    Set<String[]> duplicates = newLinkedHashSet();
    duplicates.add(array("Luke", "Yoda"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotHaveDuplicates());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveDuplicates(actual, duplicates).create());
  }

  @Test
  void should_pass_within_time_constraints() {
    // GIVEN
    List<UUID> generated = Stream.generate(UUID::randomUUID).limit(GENERATED_OBJECTS_NUMBER).toList();
    long start = System.currentTimeMillis();
    // WHEN
    assertThat(generated).doesNotHaveDuplicates();
    // THEN
    // check that it takes less than 2 seconds, usually it takes 100ms on an average computer
    // with the previous implementation, it would take minutes ...
    then(System.currentTimeMillis() - start).isLessThan(2000);
  }

  @Test
  void should_pass_if_actual_does_not_have_duplicates_according_to_custom_comparison_strategy() {
    assertThat(List.of("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotHaveDuplicates();
  }

  @Test
  void should_fail_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda", "Leia", "LUKE", "yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotHaveDuplicates());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveDuplicates(actual, newLinkedHashSet("LUKE", "yoda"),
                                                            new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_pass_within_time_constraints_with_custom_comparison_strategy() {
    // GIVEN
    List<String> generated = Stream.generate(() -> UUID.randomUUID().toString()).limit(GENERATED_OBJECTS_NUMBER).toList();
    long start = System.currentTimeMillis();
    // WHEN
    assertThat(generated).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotHaveDuplicates();
    // THEN
    // check that it takes less than 10 seconds, usually it takes 1000ms on an average computer
    // with the previous implementation, it would take minutes ...
    then(System.currentTimeMillis() - start).isLessThan(10000);
  }

}
