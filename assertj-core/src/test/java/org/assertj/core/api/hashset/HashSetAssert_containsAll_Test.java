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
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsAll_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsAll(List.of(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsAll(getInfo(assertions), getActual(assertions), List.of(someValues));
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN/THEN
    then(hashSet).containsAll(List.of("Yoda", "Luke", "Luke", "Han"));
  }

  @HashSetTest
  void should_fail_for_not_listed_elements_and_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).containsAll(List.of(first, asOriginalFirst)));
    // THEN
    var message = shouldContain(dates, new Date[] { first, asOriginalFirst }, List.of(first, asOriginalFirst)).create()
                  + "(elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)";
    then(assertionError).hasMessage(message);
  }
}
