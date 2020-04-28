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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.spliterator;

import static java.lang.String.format;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SORTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Spliterator;

import org.assertj.core.api.SpliteratorAssert;
import org.assertj.core.internal.SpliteratorsBaseTest;
import org.assertj.core.test.StringSpliterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link SpliteratorAssert#hasOnlyCharacteristics(int...)}</code>.
 *
 * @author William Bakker
 */
@DisplayName("Spliterators assertHasCharacteristics")
public class Spliterators_assertHasOnlyCharacteristics_Test extends SpliteratorsBaseTest {

  @Test
  public void should_pass_if_spliterator_has_given_single_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(DISTINCT);
    // THEN
    spliterators.assertHasOnlyCharacteristics(INFO, actual, DISTINCT);
  }

  @Test
  public void should_pass_if_spliterator_has_only_given_characteristics() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(DISTINCT | SORTED);
    // THEN
    spliterators.assertHasOnlyCharacteristics(INFO, actual, DISTINCT, SORTED);
  }

  @Test
  public void should_fail_when_spliterator_is_null() {
    // GIVEN
    Spliterator<?> nullActual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> spliterators.assertHasOnlyCharacteristics(INFO, nullActual, DISTINCT));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_spliterator_has_additional_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.SORTED | DISTINCT);
    // WHEN
    AssertionError error = expectAssertionError(() -> spliterators.assertHasOnlyCharacteristics(INFO, actual, DISTINCT));
    // THEN
    assertThat(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                        + "  <[\"DISTINCT\", \"SORTED\"]>%n"
                                        + "to contain only:%n"
                                        + "  <[\"DISTINCT\"]>%n"
                                        + "but the following characteristics were unexpected:%n"
                                        + "  <[\"SORTED\"]>%n"));
  }

  @Test
  public void should_fail_if_spliterator_does_not_have_all_expected_characteristics() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(SORTED | ORDERED);
    // WHEN
    AssertionError error = expectAssertionError(() -> spliterators.assertHasOnlyCharacteristics(INFO, actual, DISTINCT, SORTED));
    // THEN
    assertThat(error).hasMessage(format("%nExpecting spliterator characteristics:%n"
                                        + "  <[\"ORDERED\", \"SORTED\"]>%n"
                                        + "to contain only:%n"
                                        + "  <[\"DISTINCT\", \"SORTED\"]>%n"
                                        + "characteristics not found:%n"
                                        + "  <[\"DISTINCT\"]>%n"
                                        + "and characteristics not expected:%n"
                                        + "  <[\"ORDERED\"]>%n"));
  }

  private static Spliterator<?> createSpliterator(int characteristics) {
    return new StringSpliterator(characteristics);
  }

}
