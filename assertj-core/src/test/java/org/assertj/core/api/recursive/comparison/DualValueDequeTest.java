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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DualValueDeque")
class DualValueDequeTest {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void beforeEachTest() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_ignore_dual_values_with_a_null_first_value() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    DualValueDeque dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    DualValue dualValueA = dualValue(null, "A");
    DualValue dualValueB = dualValue("B", "B");
    DualValue dualValueC = dualValue(null, "C");
    DualValue dualValueD = dualValue("D", "D");
    DualValue dualValueE = dualValue("E", "E");
    // WHEN
    dualValueDeque.add(dualValueA);
    dualValueDeque.add(dualValueB);
    dualValueDeque.addFirst(dualValueC);
    dualValueDeque.add(dualValueD);
    dualValueDeque.addLast(dualValueE);
    dualValueDeque.add(1, dualValueA);
    dualValueDeque.addAll(list(dualValueA, dualValueB, dualValueC));
    dualValueDeque.addAll(0, list(dualValueA, dualValueB, dualValueC));
    // THEN
    BDDAssertions.then(dualValueDeque).containsExactly(dualValueB, dualValueB, dualValueD, dualValueE, dualValueB);
  }

  @Test
  void should_ignore_dual_values_with_a_null_second_value() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    DualValueDeque dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    DualValue dualValueA = dualValue("A", null);
    DualValue dualValueB = dualValue("B", "B");
    DualValue dualValueC = dualValue("C", null);
    DualValue dualValueD = dualValue("D", "D");
    DualValue dualValueE = dualValue("E", "E");
    // WHEN
    dualValueDeque.add(dualValueA);
    dualValueDeque.add(dualValueB);
    dualValueDeque.addFirst(dualValueC);
    dualValueDeque.add(dualValueD);
    dualValueDeque.addLast(dualValueE);
    dualValueDeque.add(1, dualValueA);
    dualValueDeque.addAll(list(dualValueA, dualValueB, dualValueC));
    dualValueDeque.addAll(0, list(dualValueA, dualValueB, dualValueC));
    // THEN
    BDDAssertions.then(dualValueDeque).containsExactly(dualValueB, dualValueB, dualValueD, dualValueE, dualValueB);
  }

  @Test
  void should_not_ignore_any_dual_values() {
    // GIVEN
    DualValueDeque dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    DualValue dualValueA = dualValue(null, "A");
    DualValue dualValueB = dualValue("B", "B");
    DualValue dualValueC = dualValue(null, "C");
    DualValue dualValueD = dualValue("D", "D");
    DualValue dualValueE = dualValue("E", "E");
    // WHEN
    dualValueDeque.add(dualValueA);
    dualValueDeque.add(dualValueB);
    dualValueDeque.addFirst(dualValueC);
    dualValueDeque.add(dualValueD);
    dualValueDeque.addLast(dualValueE);
    dualValueDeque.add(1, dualValueA);
    dualValueDeque.addAll(list(dualValueA, dualValueB, dualValueC));
    dualValueDeque.addAll(0, list(dualValueA, dualValueB, dualValueC));
    // THEN
    BDDAssertions.then(dualValueDeque).containsExactly(dualValueA, dualValueB, dualValueC, dualValueC, dualValueA, dualValueA,
                                                       dualValueB,
                                                       dualValueD, dualValueE, dualValueA, dualValueB, dualValueC);
  }

  private static DualValue dualValue(String value1, String value2) {
    return new DualValue(randomPath(), value1, value2);
  }

  static List<String> randomPath() {
    return list(RandomStringUtils.random(RandomUtils.nextInt(1, 10), true, false));
  }
}
