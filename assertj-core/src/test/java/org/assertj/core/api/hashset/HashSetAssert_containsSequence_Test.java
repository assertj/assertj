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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

class HashSetAssert_containsSequence_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsSequence(someValues).containsSequence(List.of(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, times(2)).assertContainsSequence(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second, third);
    Date[] exactElements = dates.toArray(new Date[0]);
    // WHEN/THEN
    then(dates).containsSequence(exactElements[1], exactElements[2])
               .containsSequence(dates);
  }

  @Test
  void should_fail_after_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    Date fourth = Date.from(EPOCH.plusSeconds(4));
    HashSet<Date> dates = newLinkedHashSet(first, second, third, fourth);
    third.setTime(5_000);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).containsSequence(second, third));
    // THEN
    then(assertionError).hasMessageContainingAll(STANDARD_REPRESENTATION.toStringOf(third),
                                                 "(elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)");
  }

  @Test
  void should_fail_after_hashCode_changed_for_Iterable() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    Date fourth = Date.from(EPOCH.plusSeconds(4));
    HashSet<Date> dates = newLinkedHashSet(first, second, third, fourth);
    third.setTime(5_000);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).containsSequence(List.of(second, third)));
    // THEN
    then(assertionError).hasMessageContainingAll(STANDARD_REPRESENTATION.toStringOf(third),
                                                 "(elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)");
  }
}
