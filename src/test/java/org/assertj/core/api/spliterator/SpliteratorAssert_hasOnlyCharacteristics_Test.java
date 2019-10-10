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

import com.google.common.collect.Sets;
import org.assertj.core.api.SpliteratorAssert;
import org.assertj.core.internal.SpliteratorsBaseTest;
import org.assertj.core.test.StringSpliterator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Spliterator;

import static org.assertj.core.error.ShouldContainOnly.*;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link SpliteratorAssert#hasOnlyCharacteristics(int...)}</code>.
 *
 * @author William Bakker
 */
public class SpliteratorAssert_hasOnlyCharacteristics_Test extends SpliteratorsBaseTest {

  @Test
  public void should_fail_when_spliterator_is_null() {
    // GIVEN
    Spliterator<?> nullActual = null;
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> spliterators.assertHasOnlyCharacteristics(INFO, nullActual, Spliterator.DISTINCT))
      .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_spliterator_has_only_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.DISTINCT);
    // THEN
    spliterators.assertHasOnlyCharacteristics(INFO, actual, Spliterator.DISTINCT);
  }

  @Test
  public void should_pass_if_spliterator_has_only_multiple_characteristics() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.DISTINCT | Spliterator.SORTED);
    // THEN
    spliterators.assertHasOnlyCharacteristics(INFO, actual, Spliterator.DISTINCT, Spliterator.SORTED);
  }

  @Test
  public void should_fail_if_spliterator_has_additional_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.SORTED | Spliterator.DISTINCT);
    // WHEN
    expectAssertionError(() -> spliterators.assertHasOnlyCharacteristics(INFO, actual, Spliterator.DISTINCT));
    // THEN
    verify(failures).failure(INFO, shouldContainOnly(Sets.newHashSet("DISTINCT", "SORTED"),
                                                     new String[] {"DISTINCT"},
                                                     Collections.emptyList(),
                                                     Collections.singletonList("SORTED")));
  }

  @Test
  public void should_fail_if_spliterator_does_not_have_characteristic() {
    // GIVEN
    Spliterator<?> actual = createSpliterator(Spliterator.SORTED);
    // WHEN
    expectAssertionError(() -> spliterators.assertHasOnlyCharacteristics(INFO, actual, Spliterator.DISTINCT));
    // THEN
    verify(failures).failure(INFO, shouldContainOnly(Collections.singleton("SORTED"),
                                                     new String[] {"DISTINCT"},
                                                     Collections.singletonList("DISTINCT"),
                                                     Collections.singletonList("SORTED")));
  }

  private Spliterator<?> createSpliterator(int characteristics) {
    return new StringSpliterator(characteristics);
  }

}
