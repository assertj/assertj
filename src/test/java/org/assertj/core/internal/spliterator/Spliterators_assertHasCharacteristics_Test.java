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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.spliterator;

import static java.util.Collections.singleton;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.SORTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Spliterator;

import org.assertj.core.api.SpliteratorAssert;
import org.assertj.core.internal.SpliteratorsBaseTest;
import org.assertj.core.test.StringSpliterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link SpliteratorAssert#hasCharacteristics(int...)}</code>.
 *
 * @author William Bakker
 */
@DisplayName("Spliterators assertHasCharacteristics")
public class Spliterators_assertHasCharacteristics_Test extends SpliteratorsBaseTest {

  @Test
  public void should_fail_when_spliterator_is_null() {
    // GIVEN
    Spliterator<?> nullActual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> spliterators.assertHasCharacteristics(INFO, nullActual, DISTINCT));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_spliterator_has_expected_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(DISTINCT);
    // THEN
    spliterators.assertHasCharacteristics(INFO, actual, DISTINCT);
  }

  @Test
  public void should_pass_if_spliterator_has_multiple_characteristics() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(DISTINCT | SORTED);
    // THEN
    spliterators.assertHasCharacteristics(INFO, actual, DISTINCT, SORTED);
  }

  @Test
  public void should_pass_if_spliterator_has_expected_characteristic_and_additional_ones() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(DISTINCT | SORTED);
    // THEN
    spliterators.assertHasCharacteristics(INFO, actual, DISTINCT);
  }

  @Test
  public void should_fail_if_spliterator_does_not_have_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(SORTED);
    // WHEN
    expectAssertionError(() -> spliterators.assertHasCharacteristics(INFO, actual, DISTINCT));
    // THEN
    verify(failures).failure(INFO, shouldContain(singleton("SORTED"), array("DISTINCT"), singleton("DISTINCT")));
  }

  private Spliterator<?> createSpliterator(int characteristics) {
    return new StringSpliterator(characteristics);
  }
}
