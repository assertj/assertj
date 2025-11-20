/*
 * Copyright 2012-2025 the original author or authors.
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
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsExactly_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsExactly(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsExactly(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN
    String[] exactElements = hashSetFactory.createWith("Yoda", "Luke", "Han").toArray(new String[0]);
    // THEN
    then(hashSet).containsExactly(exactElements);
  }

  @HashSetTest
  void should_fail_finding_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    first.setTime(3_000);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(dates).containsExactly(first, second));
    // THEN
    String message = shouldContainExactly(dates, List.of(first, second), List.of(first), List.of(first)).create()
                     + "(elements were checked as in HashSet, as soon as their hashCode change, the HashSet won't find them anymore - use skippingHashCodeComparison to get a collection like comparison)";
    then(assertionError).hasMessage(message);
  }
}
