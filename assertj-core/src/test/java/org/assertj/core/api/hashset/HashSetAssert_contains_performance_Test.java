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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Reproduces <a href="https://github.com/assertj/assertj/issues/4233">#4233</a>: {@code contains} and
 * {@code containsAll} used to scan the whole {@link HashSet} for each checked element, making them O(n^2).
 * <p>
 * With the fix, membership is answered by {@link HashSet#contains(Object)} in O(1), so both assertions complete
 * almost instantly; without it, the assertions below take several seconds and blow past the timeout.
 */
class HashSetAssert_contains_performance_Test {

  private static final int ACTUAL_SIZE = 100_000;

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void containsAll_should_not_scan_the_whole_set_for_each_element() {
    // GIVEN
    HashSet<Integer> actual = sequentialHashSet(ACTUAL_SIZE);
    Set<Integer> expected = new HashSet<>(actual);
    // WHEN/THEN
    assertThat(actual).containsAll(expected);
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void contains_should_not_scan_the_whole_set_for_each_element() {
    // GIVEN
    HashSet<Integer> actual = sequentialHashSet(ACTUAL_SIZE);
    Integer[] present = actual.toArray(new Integer[0]);
    // WHEN/THEN
    assertThat(actual).contains(present);
  }

  private static HashSet<Integer> sequentialHashSet(int size) {
    HashSet<Integer> set = new HashSet<>();
    for (int i = 0; i < size; i++) {
      set.add(i);
    }
    return set;
  }
}
