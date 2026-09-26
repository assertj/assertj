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
package org.assertj.guava.api;

import org.assertj.core.data.MapEntry;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Table;
import com.google.common.io.ByteSource;

/// The entry point for all Guava assertions.
///
/// @author marcelfalliere
/// @author miralak
/// @author Kornel
/// @author Jan Gorman
/// @author Joel Costigliola
/// @author Marcin Kwaczyński
/// @author Max Daniline
/// @author Ilya Koshaleu
public class Assertions implements InstanceOfAssertFactories {

  /// Creates an assertion for the given [ByteSource].
  ///
  /// @param actual the actual value
  /// @return the created assertion
  public static ByteSourceAssert assertThat(final ByteSource actual) {
    return new ByteSourceAssert(actual);
  }

  /// Creates an assertion for the given [Multimap].
  ///
  /// @param <K> the type of keys in the multimap
  /// @param <V> the type of values in the multimap
  /// @param actual the actual value
  /// @return the created assertion
  public static <K, V> MultimapAssert<K, V> assertThat(final Multimap<K, V> actual) {
    return new MultimapAssert<>(actual);
  }

  /// Creates an assertion for the given [Optional].
  ///
  /// @param <T> the type of the optional value
  /// @param actual the actual value
  /// @return the created assertion
  public static <T> OptionalAssert<T> assertThat(final Optional<T> actual) {
    return new OptionalAssert<>(actual);
  }

  /// Creates an assertion for the given [Range].
  ///
  /// @param <T> the type of values in the range
  /// @param actual the actual value
  /// @return the created assertion
  public static <T extends Comparable<T>> RangeAssert<T> assertThat(final Range<T> actual) {
    return new RangeAssert<>(actual);
  }

  /// Creates an assertion for the given [RangeMap].
  ///
  /// @param <K> the type of keys in the range map
  /// @param <V> the type of values in the range map
  /// @param actual the actual value
  /// @return the created assertion
  public static <K extends Comparable<K>, V> RangeMapAssert<K, V> assertThat(final RangeMap<K, V> actual) {
    return new RangeMapAssert<>(actual);
  }

  /// Creates an assertion for the given [RangeSet].
  ///
  /// @param <T> the type of values in the range set
  /// @param actual the actual value
  /// @return the created assertion
  public static <T extends Comparable<T>> RangeSetAssert<T> assertThat(final RangeSet<T> actual) {
    return new RangeSetAssert<>(actual);
  }

  /// Creates an assertion for the given [Table].
  ///
  /// @param <R> the type of row keys in the table
  /// @param <C> the type of column keys in the table
  /// @param <V> the type of values in the table
  /// @param actual the actual value
  /// @return the created assertion
  public static <R, C, V> TableAssert<R, C, V> assertThat(Table<R, C, V> actual) {
    return new TableAssert<>(actual);
  }

  /// Creates an assertion for the given [Multiset].
  ///
  /// @param <T> the type of values in the multiset
  /// @param actual the actual value
  /// @return the created assertion
  public static <T> MultisetAssert<T> assertThat(final Multiset<T> actual) {
    return new MultisetAssert<>(actual);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all AssertJ Guava features.
  // ------------------------------------------------------------------------------------------------------

  /// Only delegate to [MapEntry#entry(Object, Object)] so that Assertions offers a fully featured entry point to all
  /// AssertJ Guava features (but you can use [MapEntry] if you prefer).
  ///
  /// Typical usage is to call `entry` in MultimapAssert `contains` assertion as shown below :
  ///
  /// ```java
  /// Multimap<String, String> actual = ArrayListMultimap.create();
  /// actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
  /// actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
  ///
  /// assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
  /// ```
  ///
  /// @param <K> the type of the key of this entry.
  /// @param <V> the type of the value of this entry.
  /// @param key the key of the entry to create.
  /// @param value the value of the entry to create.
  ///
  /// @return the built entry
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return MapEntry.entry(key, value);
  }

  /// protected to avoid direct instantiation but allowing subclassing.
  protected Assertions() {
    // empty
  }
}
