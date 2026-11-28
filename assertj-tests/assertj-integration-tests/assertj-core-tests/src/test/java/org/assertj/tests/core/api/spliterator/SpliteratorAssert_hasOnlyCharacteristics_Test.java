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
package org.assertj.tests.core.api.spliterator;

import static java.lang.String.format;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SORTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Spliterator;

import org.assertj.tests.core.testkit.StringSpliterator;
import org.junit.jupiter.api.Test;

/**
 * @author William Bakker
 */
class SpliteratorAssert_hasOnlyCharacteristics_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Spliterator<?> actual = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasOnlyCharacteristics(DISTINCT));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_spliterator_has_given_single_characteristic() {
    assertThat(new StringSpliterator(DISTINCT)).hasOnlyCharacteristics(DISTINCT);
  }

  @Test
  void should_pass_if_spliterator_has_only_given_characteristics() {
    assertThat(new StringSpliterator(DISTINCT | SORTED)).hasOnlyCharacteristics(SORTED, DISTINCT);
  }

  @Test
  void should_pass_if_spliterator_has_no_characteristics_and_none_are_expected() {
    assertThat(new StringSpliterator()).hasOnlyCharacteristics();
  }

  @Test
  void should_fail_if_spliterator_has_additional_characteristic() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(SORTED | DISTINCT);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasOnlyCharacteristics(DISTINCT));
    // THEN
    then(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                  + "  [\"DISTINCT\", \"SORTED\"]%n"
                                  + "to contain only:%n"
                                  + "  [\"DISTINCT\"]%n"
                                  + "but the following characteristics were unexpected:%n"
                                  + "  [\"SORTED\"]%n"));
  }

  @Test
  void should_fail_if_spliterator_does_not_have_expected_characteristic() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(SORTED);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasOnlyCharacteristics(DISTINCT, SORTED));
    // THEN
    then(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                  + "  [\"SORTED\"]%n"
                                  + "to contain only:%n"
                                  + "  [\"DISTINCT\", \"SORTED\"]%n"
                                  + "but could not find the following characteristics:%n"
                                  + "  [\"DISTINCT\"]%n"));
  }

  @Test
  void should_fail_if_spliterator_does_not_have_all_expected_characteristics_and_has_unexpected_ones() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(SORTED | ORDERED);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasOnlyCharacteristics(DISTINCT, SORTED));
    // THEN
    then(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                  + "  [\"ORDERED\", \"SORTED\"]%n"
                                  + "to contain only:%n"
                                  + "  [\"DISTINCT\", \"SORTED\"]%n"
                                  + "characteristics not found:%n"
                                  + "  [\"DISTINCT\"]%n"
                                  + "and characteristics not expected:%n"
                                  + "  [\"ORDERED\"]%n"));
  }

}
