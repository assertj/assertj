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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.assertj.core.data.MapEntry;
import org.assertj.core.presentation.Representation;

public class ShouldContainEntries extends BasicErrorMessageFactory {

  public static <K, V> ErrorMessageFactory shouldContainEntries(Map<? extends K, ? extends V> actual,
                                                                Entry<? extends K, ? extends V>[] expectedEntries,
                                                                Set<Entry<? extends K, ? extends V>> entriesWithWrongValue,
                                                                Set<Entry<? extends K, ? extends V>> entriesWithKeyNotFound,
                                                                Representation representation) {
    if (entriesWithWrongValue.isEmpty()) return new ShouldContainEntries(actual, expectedEntries, entriesWithKeyNotFound);
    if (entriesWithKeyNotFound.isEmpty())
      return new ShouldContainEntries(actual, expectedEntries,
                                      buildValueDifferences(actual, entriesWithWrongValue, representation));
    // mix of missing keys and keys with different values
    return new ShouldContainEntries(actual, expectedEntries, entriesWithKeyNotFound,
                                    buildValueDifferences(actual, entriesWithWrongValue, representation));
  }

  private static <K, V> List<String> buildValueDifferences(Map<? extends K, ? extends V> actual,
                                                           Set<Entry<? extends K, ? extends V>> entriesWithWrongValues,
                                                           Representation representation) {
    return entriesWithWrongValues.stream()
                                 .map(entryWithWrongValue -> valueDifference(actual, entryWithWrongValue, representation))
                                 .collect(toList());
  }

  private static <K, V> String valueDifference(Map<? extends K, ? extends V> actual,
                                               Entry<? extends K, ? extends V> entryWithWrongValue,
                                               Representation representation) {
    K key = entryWithWrongValue.getKey();
    MapEntry<K, ? extends V> actualEntry = entry(key, actual.get(key));
    V expectedValue = entryWithWrongValue.getValue();
    return escapePercent(format("%s (expected: %s)", representation.toStringOf(actualEntry),
                                representation.toStringOf(expectedValue)));
  }

  private <K, V> ShouldContainEntries(Map<? extends K, ? extends V> actual,
                                      Entry<? extends K, ? extends V>[] expectedEntries,
                                      Set<Entry<? extends K, ? extends V>> notFound) {
    super("%nExpecting map:%n" +
          "  %s%n" +
          "to contain entries:%n" +
          "  %s%n" +
          "but could not find the following map entries:%n" +
          "  %s",
          actual, expectedEntries, notFound);
  }

  private <K, V> ShouldContainEntries(Map<? extends K, ? extends V> actual,
                                      Entry<? extends K, ? extends V>[] expectedEntries,
                                      List<String> valueDifferences) {
    super("%nExpecting map:%n" +
          "  %s%n" +
          "to contain entries:%n" +
          "  %s%n" +
          "but the following map entries had different values:%n" +
          "  " + valueDifferences,
          actual, expectedEntries, valueDifferences);
  }

  private <K, V> ShouldContainEntries(Map<? extends K, ? extends V> actual,
                                      Entry<? extends K, ? extends V>[] expectedEntries,
                                      Set<Entry<? extends K, ? extends V>> keysNotFound,
                                      List<String> valueDifferences) {
    super("%nExpecting map:%n" +
          "  %s%n" +
          "to contain entries:%n" +
          "  %s%n" +
          "but could not find the following map entries:%n" +
          "  %s%n" +
          "and the following map entries had different values:%n" +
          "  " + valueDifferences,
          actual, expectedEntries, keysNotFound);
  }

}
