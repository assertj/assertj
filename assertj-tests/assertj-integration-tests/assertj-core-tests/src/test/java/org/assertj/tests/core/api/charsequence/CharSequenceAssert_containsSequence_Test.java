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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Billy Yuan
/// @author André Diermann
class CharSequenceAssert_containsSequence_Test {

  private static final String ACTUAL = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(List.of("{ ", "'title':")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    Iterable<CharSequence> sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_sequence_values_are_empty() {
    // GIVEN
    List<CharSequence> sequence = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    assertThat(ACTUAL).containsSequence(List.of("{ ", "'title':", "'A Game of Thrones'", ","));
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    List<String> sequence = List.of("{ ", "'title':", "'A Game of Thrones'", "unexpectedString");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContain(ACTUAL, sequence.toArray(new String[0]),
                                                  newLinkedHashSet("unexpectedString")).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_the_given_order() {
    // GIVEN
    List<String> sequence = List.of("'A Game of Thrones'", "'title':");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(ACTUAL, sequence.toArray(new String[0])).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSequence(List.of("YO", "dA"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSequence(List.of("Yo", "da", "Han")));
    // THEN
    then(assertionError).hasMessage(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"),
                                                  comparisonStrategy).create());
  }

}
