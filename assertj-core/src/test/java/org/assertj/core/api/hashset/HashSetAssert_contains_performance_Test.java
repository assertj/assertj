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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Reproduces <a href="https://github.com/assertj/assertj/issues/4233">#4233</a>: {@code contains} and
 * {@code containsAll} used to scan the whole {@link HashSet} for each checked element (O(n²)). With the fix,
 * membership is answered by {@link HashSet#contains(Object)} in O(1), so both complete well under the timeout.
 */
class HashSetAssert_contains_performance_Test {

  private static final int ACTUAL_SIZE = 100_000;
  private static final int EXPECTED_SIZE = 50_000;

  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS)
  void should_pass_contains_quickly_for_large_hash_set() {
    // GIVEN
    HashSet<Integer> actual = largeHashSet(ACTUAL_SIZE);
    Integer[] expected = IntStream.range(0, EXPECTED_SIZE).boxed().toArray(Integer[]::new);
    // WHEN/THEN
    assertThat(actual).contains(expected);
  }

  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS)
  void should_pass_containsAll_quickly_for_large_hash_set() {
    // GIVEN
    HashSet<Integer> actual = largeHashSet(ACTUAL_SIZE);
    Set<Integer> expected = IntStream.range(0, EXPECTED_SIZE).boxed().collect(Collectors.toSet());
    // WHEN/THEN
    assertThat(actual).containsAll(expected);
  }

  private static HashSet<Integer> largeHashSet(int size) {
    return IntStream.range(0, size).boxed().collect(Collectors.toCollection(HashSet::new));
  }

}
