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
package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_isSubsetOf_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.isSubsetOf(List.of(someValues)).isSubsetOf(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, never()).assertIsSubsetOf(getInfo(assertions), getActual(assertions), List.of(someValues));
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Luke", "Yoda", "Han");
    // WHEN/THEN
    then(hashSet).isSubsetOf("Obi", "Qui-Gon", "Yoda", "Luke", "Han")
                 .isSubsetOf(List.of("Qui-Gon", "Luke", "Han", "Yoda"));
  }

  @HashSetTest
  void should_fail_when_hashCode_changed_as_in_superset(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    // THEN
    then(dates).isSubsetOf(second, Date.from(EPOCH.plusSeconds(3)));
  }

  @HashSetTest
  void should_fail_when_hashCode_changed_differently_than_in_superset(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).isSubsetOf(List.of(asOriginalFirst, second)));
    // THEN
    String messageStart = shouldBeSubsetOf(dates, List.of(asOriginalFirst, second), singletonList(first)).create();
    then(assertionError).hasMessageStartingWith(messageStart)
                        .hasMessageNotContaining("hashCode");
  }
}
