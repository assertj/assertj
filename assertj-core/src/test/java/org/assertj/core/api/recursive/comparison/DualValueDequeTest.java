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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
class DualValueDequeTest {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();

  @Test
  void should_ignore_dual_values_with_a_null_first_value() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    var dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    var dualValueA = dualValue(null, "A");
    var dualValueB = dualValue("B", "B");
    var dualValueC = dualValue(null, "C");
    var dualValueD = dualValue("D", "D");
    var dualValueE = dualValue("E", "E");
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
    then(dualValueDeque).containsExactly(dualValueB, dualValueB, dualValueD, dualValueE, dualValueB);
  }

  @Test
  void should_ignore_dual_values_with_a_null_second_value() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    var dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    var dualValueA = dualValue("A", null);
    var dualValueB = dualValue("B", "B");
    var dualValueC = dualValue("C", null);
    var dualValueD = dualValue("D", "D");
    var dualValueE = dualValue("E", "E");
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
    then(dualValueDeque).containsExactly(dualValueB, dualValueB, dualValueD, dualValueE, dualValueB);
  }

  @Test
  void should_not_ignore_any_dual_values() {
    // GIVEN
    var dualValueDeque = new DualValueDeque(recursiveComparisonConfiguration);
    var dualValueA = dualValue(null, "A");
    var dualValueB = dualValue("B", "B");
    var dualValueC = dualValue(null, "C");
    var dualValueD = dualValue("D", "D");
    var dualValueE = dualValue("E", "E");
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
    then(dualValueDeque).containsExactly(dualValueA, dualValueB, dualValueC, dualValueC, dualValueA, dualValueA,
                                         dualValueB, dualValueD, dualValueE, dualValueA, dualValueB, dualValueC);
  }

  private static DualValue dualValue(String value1, String value2) {
    String path = RandomStringUtils.secure().next(RandomUtils.secure().randomInt(1, 10), true, false);
    return new DualValue(new FieldLocation(path), value1, value2, null);
  }
}
