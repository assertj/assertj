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
package org.assertj.core.api.spliterator;

import org.assertj.core.api.BaseTest;
import org.assertj.core.api.SpliteratorAssert;
import org.assertj.core.test.StringSpliterator;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for <code>{@link SpliteratorAssert#hasOnlyCharacteristics(int...)}</code>.
 *
 * @author William Bakker
 */
public class SpliteratorAssert_hasOnlyCharacteristics_Test extends BaseTest {

  @Test
  public void should_fail_when_spliterator_is_null() {
    // GIVEN
    Spliterator<?> nullActual = null;
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> assertThat(nullActual).hasOnlyCharacteristics(Spliterator.DISTINCT))
      .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_spliterator_has_only_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.DISTINCT);
    // THEN
    assertThat(actual).hasOnlyCharacteristics(Spliterator.DISTINCT);
  }

  @Test
  public void should_pass_if_spliterator_has_only_multiple_characteristics() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.DISTINCT | Spliterator.SORTED);
    // THEN
    assertThat(actual).hasOnlyCharacteristics(Spliterator.DISTINCT, Spliterator.SORTED);
  }

  @Test
  public void should_fail_if_spliterator_has_additional_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.SORTED | Spliterator.DISTINCT);
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasOnlyCharacteristics(Spliterator.DISTINCT));
    // THEN
    assertThat(thrown).isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_fail_if_spliterator_does_not_have_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.SORTED);
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasOnlyCharacteristics(Spliterator.DISTINCT));
    // THEN
    assertThat(thrown).isInstanceOf(AssertionError.class);
  }

  private Spliterator<?> createSpliterator(int characteristics) {
    return new StringSpliterator(characteristics);
  }
}
