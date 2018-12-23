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
