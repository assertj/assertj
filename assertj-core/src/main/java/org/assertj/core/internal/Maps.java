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
package org.assertj.core.internal;

import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldContainEntries.shouldContainEntries;
import static org.assertj.core.error.ShouldContainEntry.shouldContainEntry;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainKey.shouldContainKey;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.error.ShouldContainValue.shouldContainValue;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonValidations.checkSizeBetween;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThan;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThan;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.data.MapEntry;
import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Map}</code>s.
 *
 * @author Alex Ruiz
 * @author Nicolas François
 * @author dorzey
 */
public class Maps {

  private static final Maps INSTANCE = new Maps();

  public static Maps instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  Maps() {}

  public <K, V> void assertAllSatisfy(AssertionInfo info, Map<K, V> actual,
                                      BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<UnsatisfiedRequirement> unsatisfiedRequirements = actual.entrySet().stream()
                                                                 .map(entry -> failsRequirements(entryRequirements, entry))
                                                                 .filter(Optional::isPresent)
                                                                 .map(Optional::get)
                                                                 .collect(toList());
    if (!unsatisfiedRequirements.isEmpty())
      throw failures.failure(info, elementsShouldSatisfy(actual, unsatisfiedRequirements, info));
  }

  private static <K, V> Optional<UnsatisfiedRequirement> failsRequirements(BiConsumer<? super K, ? super V> entryRequirements,
                                                                           Entry<K, V> entry) {
    try {
      entryRequirements.accept(entry.getKey(), entry.getValue());
    } catch (AssertionError ex) {
      return Optional.of(new UnsatisfiedRequirement(entry, ex));
    }
    return Optional.empty();
  }

  public <K, V> void assertAnySatisfy(AssertionInfo info, Map<K, V> actual,
                                      BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<UnsatisfiedRequirement> unsatisfiedRequirements = new ArrayList<>();
    for (Entry<K, V> entry : actual.entrySet()) {
      Optional<UnsatisfiedRequirement> result = failsRequirements(entryRequirements, entry);
      if (!result.isPresent()) return; // entry satisfied the requirements
      unsatisfiedRequirements.add(result.get());
    }

    throw failures.failure(info, elementsShouldSatisfyAny(actual, unsatisfiedRequirements, info));
  }

  public <K, V> void assertNoneSatisfy(AssertionInfo info, Map<K, V> actual, BiConsumer<? super K, ? super V> entryRequirements) {
    requireNonNull(entryRequirements, "The BiConsumer<K, V> expressing the assertions requirements must not be null");
    assertNotNull(info, actual);

    List<Entry<K, V>> erroneousEntries = actual.entrySet().stream()
                                               .map(entry -> failsRestrictions(entry, entryRequirements))
                                               .filter(Optional::isPresent)
                                               .map(Optional::get)
                                               .collect(toList());

    if (!erroneousEntries.isEmpty()) throw failures.failure(info, noElementsShouldSatisfy(actual, erroneousEntries));
  }

  private <V, K> Optional<Entry<K, V>> failsRestrictions(Entry<K, V> entry,
                                                         BiConsumer<? super K, ? super V> entryRequirements) {
    try {
      entryRequirements.accept(entry.getKey(), entry.getValue());
    } catch (AssertionError e) {
      // element is supposed not to meet the given restrictions
      return Optional.empty();
    }
    // element meets the given restrictions!
    return Optional.of(entry);
  }

  public void assertNullOrEmpty(AssertionInfo info, Map<?, ?> actual) {
    if (actual != null && !actual.isEmpty()) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  public void assertEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (!actual.isEmpty()) throw failures.failure(info, shouldBeEmpty(actual));
  }

  public void assertNotEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (actual.isEmpty()) throw failures.failure(info, shouldNotBeEmpty());
  }

  public void assertHasSize(AssertionInfo info, Map<?, ?> actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, actual.size(), expectedSize, info);
  }

  public void assertHasSizeGreaterThan(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThan(actual, boundary, actual.size(), info);
  }

  public void assertHasSizeGreaterThanOrEqualTo(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThanOrEqualTo(actual, boundary, actual.size(), info);
  }

  public void assertHasSizeLessThan(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThan(actual, boundary, actual.size(), info);
  }

  public void assertHasSizeLessThanOrEqualTo(AssertionInfo info, Map<?, ?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThanOrEqualTo(actual, boundary, actual.size(), info);
  }

  public void assertHasSizeBetween(AssertionInfo info, Map<?, ?> actual, int lowerBoundary, int higherBoundary) {
    assertNotNull(info, actual);
    checkSizeBetween(actual, lowerBoundary, higherBoundary, actual.size(), info);
  }

  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Iterable<?> other) {
    assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Object other) {
    assertNotNull(info, map);
    assertIsArray(info, other);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Map<?, ?> other) {
    assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  public <K, V> void assertContains(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptySinceActualIsNotEmpty(info, actual, entries);
    failIfAnyEntryNotFoundInActualMap(info, actual, entries);
  }

  @SuppressWarnings("unchecked")
  public <K, V> void assertContainsAllEntriesOf(AssertionInfo info, Map<K, V> actual, Map<? extends K, ? extends V> other) {
    failIfNull(other);
    assertNotNull(info, actual);
    // assertion passes if other is empty since actual contains all other entries.
    if (other.isEmpty()) return;
    failIfAnyEntryNotFoundInActualMap(info, actual, other.entrySet().toArray(new Entry[0]));
  }

  public <K, V> void assertContainsAnyOf(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptySinceActualIsNotEmpty(info, actual, entries);
    for (Entry<? extends K, ? extends V> entry : entries) {
      if (containsEntry(actual, entry)) return;
    }
    throw failures.failure(info, shouldContainAnyOf(actual, entries));
  }

  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual, K key, Condition<? super V> valueCondition) {
    assertContainsKey(info, actual, key);
    conditions.assertIsNotNull(valueCondition);
    V value = actual.get(key);
    if (!valueCondition.matches(value)) throw failures.failure(info, elementsShouldBe(actual, value, valueCondition));
  }

  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual, K key,
                                              Consumer<? super V> valueRequirements) {
    assertContainsKey(info, actual, key);
    requireNonNull(valueRequirements, "The Consumer<V> expressing the assertions requirements must not be null");
    V value = actual.get(key);
    valueRequirements.accept(value);
  }

  public <K, V> void assertHasEntrySatisfying(AssertionInfo info, Map<K, V> actual,
                                              Condition<? super Entry<K, V>> entryCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(entryCondition);
    for (Entry<K, V> entry : actual.entrySet()) {
      if (entryCondition.matches(entry)) return;
    }

    throw failures.failure(info, shouldContainEntry(actual, entryCondition));
  }

  public <K, V> void assertHasEntrySatisfyingConditions(AssertionInfo info, Map<K, V> actual, Condition<? super K> keyCondition,
                                                        Condition<? super V> valueCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(keyCondition, "The condition to evaluate for entries key should not be null");
    conditions.assertIsNotNull(valueCondition, "The condition to evaluate for entries value should not be null");

    for (Entry<K, V> entry : actual.entrySet()) {
      if (keyCondition.matches(entry.getKey()) && valueCondition.matches(entry.getValue())) return;
    }

    throw failures.failure(info, shouldContainEntry(actual, keyCondition, valueCondition));
  }

  public <K> void assertHasKeySatisfying(AssertionInfo info, Map<K, ?> actual, Condition<? super K> keyCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(keyCondition);

    for (K key : actual.keySet()) {
      if (keyCondition.matches(key)) return;
    }

    throw failures.failure(info, shouldContainKey(actual, keyCondition));
  }

  public <V> void assertHasValueSatisfying(AssertionInfo info, Map<?, V> actual, Condition<? super V> valueCondition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(valueCondition);

    for (V value : actual.values()) {
      if (valueCondition.matches(value)) return;
    }

    throw failures.failure(info, shouldContainValue(actual, valueCondition));
  }

  public <K, V> void assertDoesNotContain(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    failIfNullOrEmpty(entries);
    assertNotNull(info, actual);
    Set<Entry<? extends K, ? extends V>> found = new LinkedHashSet<>();
    for (Entry<? extends K, ? extends V> entry : entries) {
      if (containsEntry(actual, entry)) {
        found.add(entry);
      }
    }
    if (!found.isEmpty()) throw failures.failure(info, shouldNotContain(actual, entries, found));
  }

  public <K, V> void assertContainsKeys(AssertionInfo info, Map<K, V> actual, K[] keys) {
    assertNotNull(info, actual);
    requireNonNull(keys, keysToLookForIsNull("array of keys"));
    if (actual.isEmpty() && keys.length == 0) return;
    failIfEmpty(keys, keysToLookForIsEmpty("array of keys"));

    Set<K> notFound = getNotFoundKeys(actual, keys);
    if (!notFound.isEmpty()) throw failures.failure(info, shouldContainKeys(actual, notFound));
  }

  public <K, V> void assertContainsKey(AssertionInfo info, Map<K, V> actual, K key) {
    assertContainsKeys(info, actual, array(key));
  }

  public <K, V> void assertDoesNotContainKey(AssertionInfo info, Map<K, V> actual, K key) {
    assertNotNull(info, actual);
    if (containsKey(actual, key)) throw failures.failure(info, shouldNotContainKey(actual, key));
  }

  public <K, V> void assertDoesNotContainKeys(AssertionInfo info, Map<K, V> actual, K[] keys) {
    assertNotNull(info, actual);
    requireNonNull(keys, keysToLookForIsNull("array of keys"));
    Set<K> found = getFoundKeys(actual, keys);
    if (!found.isEmpty()) throw failures.failure(info, shouldNotContainKeys(actual, found));
  }

  public <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual, K[] keys) {
    assertContainsOnlyKeys(info, actual, "array of keys", keys);
  }

  public <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual, Iterable<? extends K> keys) {
    assertContainsOnlyKeys(info, actual, "keys iterable", toArray(keys));
  }

  private <K, V> void assertContainsOnlyKeys(AssertionInfo info, Map<K, V> actual, String placeholderForErrorMessages, K[] keys) {
    assertNotNull(info, actual);
    requireNonNull(keys, keysToLookForIsNull(placeholderForErrorMessages));
    if (actual.isEmpty() && keys.length == 0) {
      return;
    }
    failIfEmpty(keys, keysToLookForIsEmpty(placeholderForErrorMessages));

    Set<K> notFound = getNotFoundKeys(actual, keys);
    Set<K> notExpected = getNotExpectedKeys(actual, keys);

    if (!notFound.isEmpty() || !notExpected.isEmpty())
      throw failures.failure(info, shouldContainOnlyKeys(actual, keys, notFound, notExpected));
  }

  private static <K> Set<K> getFoundKeys(Map<K, ?> actual, K[] expectedKeys) {
    // Stream API avoided for performance reasons
    Set<K> found = new LinkedHashSet<>();
    for (K expectedKey : expectedKeys) {
      if (containsKey(actual, expectedKey)) found.add(expectedKey);
    }
    return found;
  }

  private static <K> Set<K> getNotFoundKeys(Map<K, ?> actual, K[] expectedKeys) {
    // Stream API avoided for performance reasons
    Set<K> notFound = new LinkedHashSet<>();
    for (K expectedKey : expectedKeys) {
      if (!containsKey(actual, expectedKey)) notFound.add(expectedKey);
    }
    return notFound;
  }

  private static <K> boolean containsKey(Map<K, ?> actual, K key) {
    try {
      return actual.containsKey(key);
    } catch (NullPointerException e) {
      if (key == null) return false; // null keys not permitted
      throw e;
    }
  }

  private static <K> Set<K> getNotExpectedKeys(Map<K, ?> actual, K[] expectedKeys) {
    // Stream API avoided for performance reasons
    try {
      Map<K, ?> clonedMap = clone(actual);
      for (K expectedKey : expectedKeys) {
        clonedMap.remove(expectedKey);
      }
      return clonedMap.keySet();
    } catch (NoSuchMethodException | RuntimeException e) {
      // actual cannot be cloned or is unmodifiable, falling back to LinkedHashMap
      Map<K, ?> copiedMap = new LinkedHashMap<>(actual);
      for (K expectedKey : expectedKeys) {
        copiedMap.remove(expectedKey);
      }
      return copiedMap.keySet();
    }
  }

  @SuppressWarnings("unchecked")
  private static <K, V> Map<K, V> clone(Map<K, V> map) throws NoSuchMethodException {
    if (isMultiValueMapAdapterInstance(map)) throw new IllegalArgumentException("Cannot clone MultiValueMapAdapter");

    try {
      if (map instanceof Cloneable) {
        return (Map<K, V>) map.getClass().getMethod("clone").invoke(map);
      }

      try {
        // try with copying constructor
        return map.getClass().getConstructor(map.getClass()).newInstance(map);
      } catch (NoSuchMethodException e) {
        // try with default constructor
        Map<K, V> newMap = map.getClass().getConstructor().newInstance();
        newMap.putAll(map);
        return newMap;
      }
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new IllegalStateException(e);
    }
  }

  private static boolean isMultiValueMapAdapterInstance(Map<?, ?> map) {
    return isInstanceOf(map, "org.springframework.util.MultiValueMapAdapter");
  }

  private static boolean isInstanceOf(Object object, String className) {
    try {
      Class<?> type = Class.forName(className);
      return type.isInstance(object);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public <K, V> void assertContainsValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (!containsValue(actual, value)) throw failures.failure(info, shouldContainValue(actual, value));
  }

  public <K, V> void assertContainsValues(AssertionInfo info, Map<K, V> actual, V[] values) {
    assertNotNull(info, actual);
    requireNonNull(values, "The array of values to look for should not be null");
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values, valuesToLookForIsEmpty());

    Set<V> notFound = getNotFoundValues(actual, values);
    if (!notFound.isEmpty()) throw failures.failure(info, shouldContainValues(actual, notFound));
  }

  public <K, V> void assertDoesNotContainValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (containsValue(actual, value)) throw failures.failure(info, shouldNotContainValue(actual, value));
  }

  private static <V> Set<V> getNotFoundValues(Map<?, V> actual, V[] expectedValues) {
    // Stream API avoided for performance reasons
    Set<V> notFound = new LinkedHashSet<>();
    for (V expectedValue : expectedValues) {
      if (!containsValue(actual, expectedValue)) notFound.add(expectedValue);
    }
    return notFound;
  }

  private static <V> boolean containsValue(Map<?, V> actual, V value) {
    try {
      return actual.containsValue(value);
    } catch (NullPointerException e) {
      if (value == null) return false; // null values not permitted
      throw e;
    }
  }

  public <K, V> void assertContainsOnly(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    doCommonContainsCheck(info, actual, entries);
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptySinceActualIsNotEmpty(info, actual, entries);

    Set<Entry<? extends K, ? extends V>> notFound = getNotFoundEntries(actual, entries);
    Set<Entry<K, V>> notExpected = getNotExpectedEntries(actual, entries);

    if (!(notFound.isEmpty() && notExpected.isEmpty()))
      throw failures.failure(info, shouldContainOnly(actual, entries, notFound, notExpected));
  }

  private static <K, V> Set<Entry<? extends K, ? extends V>> getNotFoundEntries(Map<K, V> actual,
                                                                                Entry<? extends K, ? extends V>[] entries) {
    // Stream API avoided for performance reasons
    Set<Entry<? extends K, ? extends V>> notFound = new LinkedHashSet<>();
    for (Entry<? extends K, ? extends V> entry : entries) {
      if (!containsEntry(actual, entry)) notFound.add(entry);
    }
    return notFound;
  }

  private static <K, V> Set<Entry<K, V>> getNotExpectedEntries(Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    // Stream API avoided for performance reasons
    Set<Entry<K, V>> notExpected = new LinkedHashSet<>();
    for (Entry<K, V> entry : mapWithoutExpectedEntries(actual, entries).entrySet()) {
      MapEntry<K, V> mapEntry = entry(entry.getKey(), entry.getValue());
      notExpected.add(mapEntry);
    }
    return notExpected;
  }

  private static <K, V> Map<K, V> mapWithoutExpectedEntries(Map<K, V> actual, Entry<? extends K, ? extends V>[] expectedEntries) {
    try {
      Map<K, V> clonedMap = clone(actual);
      removeEntries(clonedMap, expectedEntries);
      return clonedMap;
    } catch (NoSuchMethodException | RuntimeException e) {
      // actual cannot be cloned or is unmodifiable, falling back to LinkedHashMap
      Map<K, V> copiedMap = new LinkedHashMap<>(actual);
      removeEntries(copiedMap, expectedEntries);
      return copiedMap;
    }
  }

  private static <K, V> void removeEntries(Map<K, V> map, Entry<? extends K, ? extends V>[] entries) {
    // Stream API avoided for performance reasons
    for (Entry<? extends K, ? extends V> entry : entries) {
      // must perform deep equals comparison on values as Map.remove(Object, Object) relies on
      // Objects.equals which does not handle deep equality (e.g. arrays in map entry values)
      if (containsEntry(map, entry)) map.remove(entry.getKey());
    }
  }

  public <K, V> void assertContainsExactly(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    doCommonContainsCheck(info, actual, entries);
    if (actual.isEmpty() && entries.length == 0) return;
    failIfEntriesIsEmptySinceActualIsNotEmpty(info, actual, entries);
    assertHasSameSizeAs(info, actual, entries);

    Set<Entry<? extends K, ? extends V>> notFound = new LinkedHashSet<>();
    Set<Entry<? extends K, ? extends V>> notExpected = new LinkedHashSet<>();

    compareActualMapAndExpectedEntries(actual, entries, notExpected, notFound);

    if (notExpected.isEmpty() && notFound.isEmpty()) {
      // check entries order
      int index = 0;
      for (K keyFromActual : actual.keySet()) {
        if (!deepEquals(keyFromActual, entries[index].getKey())) {
          Entry<K, V> actualEntry = entry(keyFromActual, actual.get(keyFromActual));
          throw failures.failure(info, elementsDifferAtIndex(actualEntry, entries[index], index));
        }
        index++;
      }
      // all entries are in the same order.
      return;
    }

    throw failures.failure(info, shouldContainExactly(actual, asList(entries), notFound, notExpected));
  }

  private <K, V> void compareActualMapAndExpectedEntries(Map<K, V> actual, Entry<? extends K, ? extends V>[] entries,
                                                         Set<Entry<? extends K, ? extends V>> notExpected,
                                                         Set<Entry<? extends K, ? extends V>> notFound) {
    Map<K, V> expectedEntries = entriesToMap(entries);
    Map<K, V> actualEntries = new LinkedHashMap<>(actual);
    for (Entry<K, V> entry : expectedEntries.entrySet()) {
      if (containsEntry(actualEntries, entry(entry.getKey(), entry.getValue()))) {
        // this is an expected entry
        actualEntries.remove(entry.getKey());
      } else {
        // this is a not found entry
        notFound.add(entry(entry.getKey(), entry.getValue()));
      }
    }
    // All remaining entries from actual copy are not expected entries.
    for (Entry<K, V> entry : actualEntries.entrySet()) {
      notExpected.add(entry(entry.getKey(), entry.getValue()));
    }
  }

  private <K, V> void doCommonContainsCheck(AssertionInfo info, Map<K, V> actual, Entry<? extends K, ? extends V>[] entries) {
    assertNotNull(info, actual);
    failIfNull(entries);
  }

  private <K, V> void failIfAnyEntryNotFoundInActualMap(AssertionInfo info, Map<K, V> actual,
                                                        Entry<? extends K, ? extends V>[] entries) {
    Set<Entry<? extends K, ? extends V>> entriesWithKeyNotFound = new LinkedHashSet<>();
    Set<Entry<? extends K, ? extends V>> entriesWithWrongValue = new LinkedHashSet<>();
    for (Entry<? extends K, ? extends V> entry : entries) {
      requireNonNull(entry, ErrorMessages.entryToLookForIsNull());
      if (!actual.containsKey(entry.getKey())) entriesWithKeyNotFound.add(entry);
      else if (!containsEntry(actual, entry)) entriesWithWrongValue.add(entry); // can only be wrong value since key was found
    }
    if (!entriesWithWrongValue.isEmpty() || !entriesWithKeyNotFound.isEmpty())
      throw failures.failure(info, shouldContainEntries(actual, entries, entriesWithWrongValue, entriesWithKeyNotFound,
                                                        info.representation()));
  }

  private static <K, V> Map<K, V> entriesToMap(Entry<? extends K, ? extends V>[] entries) {
    Map<K, V> expectedEntries = new LinkedHashMap<>();
    for (Entry<? extends K, ? extends V> entry : entries) {
      expectedEntries.put(entry.getKey(), entry.getValue());
    }
    return expectedEntries;
  }

  private static <K> void failIfEmpty(K[] keys, String errorMessage) {
    checkArgument(keys.length > 0, errorMessage);
  }

  private static <K, V> void failIfEmpty(Entry<? extends K, ? extends V>[] entries) {
    checkArgument(entries.length > 0, "The array of entries to look for should not be empty");
  }

  private static <K, V> void failIfNullOrEmpty(Entry<? extends K, ? extends V>[] entries) {
    failIfNull(entries);
    failIfEmpty(entries);
  }

  private static <K, V> void failIfNull(Entry<? extends K, ? extends V>[] entries) {
    requireNonNull(entries, ErrorMessages.entriesToLookForIsNull());
  }

  private static <K, V> void failIfNull(Map<? extends K, ? extends V> map) {
    requireNonNull(map, ErrorMessages.mapOfEntriesToLookForIsNull());
  }

  private static <K, V> boolean containsEntry(Map<K, V> actual, Entry<? extends K, ? extends V> entry) {
    requireNonNull(entry, ErrorMessages.entryToLookForIsNull());
    return actual.containsKey(entry.getKey()) && deepEquals(actual.get(entry.getKey()), entry.getValue());
  }

  private void assertNotNull(AssertionInfo info, Map<?, ?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  // this should be only called when actual is not empty
  private <K, V> void failIfEntriesIsEmptySinceActualIsNotEmpty(AssertionInfo info, Map<K, V> actual,
                                                                Entry<? extends K, ? extends V>[] entries) {
    if (entries.length == 0) throw failures.failure(info, shouldBeEmpty(actual));
  }

}
