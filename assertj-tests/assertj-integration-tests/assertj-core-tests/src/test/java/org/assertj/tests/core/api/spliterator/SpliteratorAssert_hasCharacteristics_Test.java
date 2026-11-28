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
class SpliteratorAssert_hasCharacteristics_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Spliterator<?> actual = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasCharacteristics(DISTINCT));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_spliterator_has_expected_characteristic() {
    assertThat(new StringSpliterator(DISTINCT)).hasCharacteristics(DISTINCT);
  }

  @Test
  void should_pass_if_spliterator_has_multiple_characteristics() {
    assertThat(new StringSpliterator(DISTINCT | SORTED)).hasCharacteristics(DISTINCT, SORTED);
  }

  @Test
  void should_pass_if_spliterator_has_combined_expected_characteristics() {
    assertThat(new StringSpliterator(DISTINCT | SORTED)).hasCharacteristics(DISTINCT | SORTED);
  }

  @Test
  void should_pass_if_spliterator_has_expected_characteristic_and_additional_ones() {
    assertThat(new StringSpliterator(DISTINCT | SORTED)).hasCharacteristics(DISTINCT);
  }

  @Test
  void should_pass_if_spliterator_has_no_characteristics_and_none_are_expected() {
    assertThat(new StringSpliterator()).hasCharacteristics();
  }

  @Test
  void should_fail_if_spliterator_has_characteristics_and_none_are_expected() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(SORTED);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasCharacteristics());
    // THEN
    then(error).hasMessage(format("%nActual:%n  [\"SORTED\"]%nis not empty while group of values to look for is."));
  }

  @Test
  void should_fail_if_spliterator_does_not_have_characteristic() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(SORTED);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasCharacteristics(DISTINCT));
    // THEN
    then(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                  + "  [\"SORTED\"]%n"
                                  + "to contain:%n"
                                  + "  [\"DISTINCT\"]%n"
                                  + "but could not find the following characteristics:%n"
                                  + "  [\"DISTINCT\"]%n"));
  }

  @Test
  void should_fail_if_spliterator_does_not_have_some_characteristics() {
    // GIVEN
    Spliterator<?> actual = new StringSpliterator(ORDERED | SORTED);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasCharacteristics(SORTED, DISTINCT, ORDERED));
    // THEN
    then(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                  + "  [\"ORDERED\", \"SORTED\"]%n"
                                  + "to contain:%n"
                                  + "  [\"DISTINCT\", \"ORDERED\", \"SORTED\"]%n"
                                  + "but could not find the following characteristics:%n"
                                  + "  [\"DISTINCT\"]%n"));
  }

}
