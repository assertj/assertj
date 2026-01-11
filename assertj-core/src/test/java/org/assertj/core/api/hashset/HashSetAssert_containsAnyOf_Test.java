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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsAnyOf_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsAnyOf(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsAnyOf(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hasSet = hashSetFactory.createWith("Yoda", "Luke", "Han");
    // WHEN/THEN
    then(hasSet).containsAnyOf("Han", "Qui-Gon");
  }

  @HashSetTest
  void should_fail_for_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).containsAnyOf(first, asOriginalFirst));
    // THEN
    String message = shouldContainAnyOf(dates, new Date[] { first, asOriginalFirst }).create()
                     + " (elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)";
    then(assertionError).hasMessage(message);
  }
}
