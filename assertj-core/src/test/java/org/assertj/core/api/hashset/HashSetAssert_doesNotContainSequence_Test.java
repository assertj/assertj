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
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

class HashSetAssert_doesNotContainSequence_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.doesNotContainSequence(someValues).doesNotContainSequence(List.of(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, times(2)).assertDoesNotContainSequence(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetTest
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second, third);
    // WHEN
    Date[] exactElements = hashSetFactory.createWith(third, first).toArray(new Date[0]);
    // THEN
    then(dates).doesNotContainSequence(exactElements)
               .doesNotContainSequence(List.of(exactElements));
  }

  @HashSetTest
  void should_fail_in_ordinary_scenario(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> toTest = hashSetFactory.createWith("Yoda", "Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(toTest).doesNotContainSequence(toTest));
    // THEN
    then(assertionError).hasMessageStartingWith(shouldNotContainSequence(toTest, toArray(toTest), 0).create());
  }

  @Test
  void should_pass_after_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = newLinkedHashSet(first, second, third);
    first.setTime(4_000);
    // WHEN/THEN
    then(dates).doesNotContainSequence(first, second);
  }

  @Test
  void should_fail_for_null() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat((HashSet<String>) null).doesNotContainSequence(""));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }
}
