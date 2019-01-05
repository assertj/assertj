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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DualDequeTest {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void beforeEachTest() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_ignore_dual_keys_with_a_null_first_key() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    DualKeyDeque dualKayDeque = new DualKeyDeque(recursiveComparisonConfiguration);
    DualKey dualKeyA = dualKey(null, "A");
    DualKey dualKeyB = dualKey("B", "B");
    DualKey dualKeyC = dualKey(null, "C");
    DualKey dualKeyD = dualKey("D", "D");
    DualKey dualKeyE = dualKey("E", "E");
    // WHEN
    dualKayDeque.add(dualKeyA);
    dualKayDeque.add(dualKeyB);
    dualKayDeque.addFirst(dualKeyC);
    dualKayDeque.add(dualKeyD);
    dualKayDeque.addLast(dualKeyE);
    dualKayDeque.add(1, dualKeyA);
    dualKayDeque.addAll(list(dualKeyA, dualKeyB, dualKeyC));
    dualKayDeque.addAll(0, list(dualKeyA, dualKeyB, dualKeyC));
    // THEN
    assertThat(dualKayDeque).containsExactly(dualKeyB, dualKeyB, dualKeyD, dualKeyE, dualKeyB);
  }

  @Test
  public void should_not_ignore_any_dual_keys() {
    // GIVEN
    DualKeyDeque dualKayDeque = new DualKeyDeque(recursiveComparisonConfiguration);
    DualKey dualKeyA = dualKey(null, "A");
    DualKey dualKeyB = dualKey("B", "B");
    DualKey dualKeyC = dualKey(null, "C");
    DualKey dualKeyD = dualKey("D", "D");
    DualKey dualKeyE = dualKey("E", "E");
    // WHEN
    dualKayDeque.add(dualKeyA);
    dualKayDeque.add(dualKeyB);
    dualKayDeque.addFirst(dualKeyC);
    dualKayDeque.add(dualKeyD);
    dualKayDeque.addLast(dualKeyE);
    dualKayDeque.add(1, dualKeyA);
    dualKayDeque.addAll(list(dualKeyA, dualKeyB, dualKeyC));
    dualKayDeque.addAll(0, list(dualKeyA, dualKeyB, dualKeyC));
    // THEN
    assertThat(dualKayDeque).containsExactly(dualKeyA, dualKeyB, dualKeyC, dualKeyC, dualKeyA, dualKeyA, dualKeyB, dualKeyD,
                                             dualKeyE, dualKeyA, dualKeyB, dualKeyC);
  }

  private static DualKey dualKey(String key1, String key2) {
    return new DualKey(randomPath(), key1, key2);
  }

  private static List<String> randomPath() {
    return list(RandomStringUtils.random(RandomUtils.nextInt(0, 10)));
  }

}
