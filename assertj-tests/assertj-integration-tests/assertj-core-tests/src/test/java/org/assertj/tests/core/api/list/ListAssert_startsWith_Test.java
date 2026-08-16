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
package org.assertj.tests.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ListAssert_startsWith_Test {

  private final Stream<String> infiniteStream = Stream.generate(() -> "");

  @Test
  @Disabled
  void should_support_infinite_streams() {
    // TODO it is not possible for startsWith to support both infinite streams and assertion chaining
    // assertion chaining has been chosen over infinite streams support
    assertThat(infiniteStream).startsWith("", "");
  }

  @Test
  void should_reuse_stream_after_startsWith_assertion() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN/THEN
    assertThat(actual).startsWith(array("Luke", "Leia"))
                      .endsWith("Leia");
  }

  @Test
  void should_fail_if_sequence_is_null() {
    // GIVEN/WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(infiniteStream).startsWith((String[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_sequence_are_empty() {
    // GIVEN
    Stream<Object> actual = Stream.of();
    // WHEN/THEN
    assertThat(actual).startsWith(emptyArray());
  }

  @Test
  void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN
    expectAssertionError(() -> assertThat(actual).startsWith());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Stream<Object> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).startsWith(emptyArray()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    String[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN
    expectAssertionError(() -> assertThat(actual).startsWith(sequence));
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence() {
    // GIVEN
    String[] sequence = { "Han", "C-3PO" };
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN
    expectAssertionError(() -> assertThat(actual).startsWith(sequence));
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    // GIVEN
    String[] sequence = { "Luke", "Yoda" };
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN
    expectAssertionError(() -> assertThat(actual).startsWith(sequence));
  }

  @Test
  void should_pass_if_actual_starts_with_sequence() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia", "Yoda");
    // WHEN/THEN
    assertThat(actual).startsWith(array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN/THEN
    assertThat(actual).startsWith(array("Luke", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    String[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                 .startsWith(sequence));
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    String[] sequence = { "Luke", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                 .startsWith(sequence));
  }

  @Test
  void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    String[] sequence = { "LUKE" };
    // WHEN/THEN
    assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .startsWith(sequence);
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    String[] sequence = { "LUKE", "lEIA" };
    // WHEN/THEN
    assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .startsWith(sequence);
  }

}
