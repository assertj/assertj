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
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_doesNotContainAnyElementsOf_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.doesNotContainAnyElementsOf(List.of(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertDoesNotContainAnyElementsOf(getInfo(assertions), getActual(assertions), List.of(someValues));
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hasSet = hashSetFactory.createWith("Yoda", "Luke", "Han");
    // WHEN/THEN
    then(hasSet).doesNotContainAnyElementsOf(List.of("Qui-Gon", "Jar Jar"));
  }

  @HashSetTest
  void should_pass_for_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    // WHEN/THEN
    then(dates).doesNotContainAnyElementsOf(List.of(first, asOriginalFirst));
  }
}
