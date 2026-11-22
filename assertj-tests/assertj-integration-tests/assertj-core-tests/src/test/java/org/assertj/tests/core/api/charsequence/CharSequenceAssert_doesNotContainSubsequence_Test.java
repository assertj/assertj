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
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainSubsequenceOfCharSequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class CharSequenceAssert_doesNotContainSubsequence_Test {

  private static final String ACTUAL = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSubsequence(List.of("A Game of Thrones")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    Iterable<CharSequence> subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_empty() {
    // GIVEN
    List<CharSequence> subsequence = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_does_not_contain_subsequence() {
    assertThat(ACTUAL).doesNotContainSubsequence(List.of("A Game of ", "Shadows"));
    assertThat(ACTUAL).doesNotContainSubsequence(List.of("George Martin", "A Game of Thrones"));
  }

  @Test
  void should_fail_if_actual_contains_subsequence() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(List.of("{", "A Game of ",
                                                                                                         "George Martin", "}")));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, array("{", "A Game of ", "George Martin", "}"),
                                                                new int[] { 0, 11, 41, 56 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_according_to_the_custom_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainSubsequence(List.of("A GAME OF ",
                                                                                                         "THRONES")));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, array("A GAME OF ", "THRONES"), new int[] { 11, 21 },
                                                                comparisonStrategy).create());
  }

}
