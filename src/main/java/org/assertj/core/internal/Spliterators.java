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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.internal.Comparables.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Spliterator}</code>s.
 *
 * @author William Bakker
 */
public class Spliterators {
  /**
   * Name for constant {@link Spliterator#DISTINCT}
   */
  private static final String SPLITERATOR_DISTINCT = "DISTINCT";
  /**
   * Name for constant {@link Spliterator#SORTED}
   */
  private static final String SPLITERATOR_SORTED = "SORTED";
  /**
   * Name for constant {@link Spliterator#ORDERED}
   */
  private static final String SPLITERATOR_ORDERED = "ORDERED";
  /**
   * Name for constant {@link Spliterator#SIZED}
   */
  private static final String SPLITERATOR_SIZED = "SIZED";
  /**
   * Name for constant {@link Spliterator#NONNULL}
   */
  private static final String SPLITERATOR_NONNULL = "NONNULL";
  /**
   * Name for constant {@link Spliterator#IMMUTABLE}
   */
  private static final String SPLITERATOR_IMMUTABLE = "IMMUTABLE";
  /**
   * Name for constant {@link Spliterator#CONCURRENT}
   */
  private static final String SPLITERATOR_CONCURRENT = "CONCURRENT";
  /**
   * Name for constant {@link Spliterator#SUBSIZED}
   */
  private static final String SPLITERATOR_SUBSIZED = "SUBSIZED";

  private static final Spliterators INSTANCE = new Spliterators();

  private final Iterables iterables = Iterables.instance();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Spliterators instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  void setFailures(Failures failures) {
    iterables.failures = failures;
  }

  /**
   * Asserts the given <code>{@link Spliterator}</code> has the given characteristics.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Spliterator}.
   * @param characteristics the expected characteristics.
   * @throws AssertionError if the actual {@code Spliterator} is {@code null}.
   * @throws AssertionError if the actual {@code Spliterator} does not have the expected characteristics.
   */
  public void assertHasCharacteristics(AssertionInfo info, Spliterator<?> actual, int... characteristics) {
    assertNotNull(info, actual);
    Set<String> actualCharacteristicNames = characteristicNames(actual.characteristics());
    Set<String> expectedCharacteristicNames = characteristicNames(characteristics);
    iterables.assertContains(info, actualCharacteristicNames, expectedCharacteristicNames.toArray(new String[0]));
  }

  /**
   * Asserts the given <code>{@link Spliterator}</code> has only the given characteristics and no else.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Spliterator}.
   * @param characteristics the expected characteristics.
   * @throws AssertionError if the actual {@code Spliterator} is {@code null}.
   * @throws AssertionError if the actual {@code Spliterator} does not have the expected characteristics
   *                        or the actual {@code Spliterator} has additional characteristics.
   */
  public void assertHasOnlyCharacteristics(AssertionInfo info, Spliterator<?> actual, int... characteristics) {
    assertNotNull(info, actual);
    Set<String> actualCharacteristicNames = characteristicNames(actual.characteristics());
    Set<String> expectedCharacteristicNames = characteristicNames(characteristics);
    iterables.assertContainsOnly(info, actualCharacteristicNames, expectedCharacteristicNames.toArray(new String[0]));
  }

  private static Set<String> characteristicNames(int[] characteristics) {
    Set<String> names = new HashSet<>();
    for (int characteristic : characteristics) {
      names.addAll(characteristicNames(characteristic));
    }
    return names;
  }

  private static Set<String> characteristicNames(int characteristics) {
    Set<String> names = new HashSet<>();
    if (hasCharacteristic(characteristics, Spliterator.DISTINCT)) names.add(SPLITERATOR_DISTINCT);
    if (hasCharacteristic(characteristics, Spliterator.SORTED)) names.add(SPLITERATOR_SORTED);
    if (hasCharacteristic(characteristics, Spliterator.ORDERED)) names.add(SPLITERATOR_ORDERED);
    if (hasCharacteristic(characteristics, Spliterator.SIZED)) names.add(SPLITERATOR_SIZED);
    if (hasCharacteristic(characteristics, Spliterator.NONNULL)) names.add(SPLITERATOR_NONNULL);
    if (hasCharacteristic(characteristics, Spliterator.IMMUTABLE)) names.add(SPLITERATOR_IMMUTABLE);
    if (hasCharacteristic(characteristics, Spliterator.CONCURRENT)) names.add(SPLITERATOR_CONCURRENT);
    if (hasCharacteristic(characteristics, Spliterator.SUBSIZED)) names.add(SPLITERATOR_SUBSIZED);
    return names;
  }

  private static boolean hasCharacteristic(int characteristics, int expectedCharacteristic) {
    return (characteristics & expectedCharacteristic) == expectedCharacteristic;
  }
}
