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
package org.assertj.core.api;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import org.assertj.core.error.GroupTypeDescription;

/// Assertions for [Spliterator] type.
///
/// @param <SELF> the "self" type of this assertion class.
/// @param <ELEMENT> the type of elements.
/// @author William Bakker
public class AbstractSpliteratorAssert<SELF extends AbstractSpliteratorAssert<SELF, ELEMENT>, ELEMENT> extends
    AbstractAssertWithComparator<SELF, Spliterator<ELEMENT>> {

  private static final GroupTypeDescription CHARACTERISTICS_GROUP_DESCRIPTION = new GroupTypeDescription("spliterator characteristics",
                                                                                                         "characteristics");

  /// Creates a new [Spliterator] assertion.
  ///
  /// @param actual the actual spliterator to verify
  /// @param selfType the type of the concrete assertion
  protected AbstractSpliteratorAssert(Spliterator<ELEMENT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /// Asserts the actual [Spliterator] has the given characteristics.
  ///
  /// Example:
  /// ```java
  /// Spliterator<Integer> spliterator = Stream.of(1, 2, 3).spliterator();
  ///
  /// // this assertion succeeds:
  /// assertThat(spliterator).hasCharacteristics(Spliterator.SIZED, Spliterator.ORDERED);
  ///
  /// // this assertion fails as the Spliterator does not have characteristic DISTINCT:
  /// assertThat(spliterator).hasCharacteristics(Spliterator.DISTINCT);
  /// ```
  ///
  /// @param characteristics the expected characteristics.
  /// @return `this` assertion object.
  /// @throws AssertionError if the actual `Spliterator` is `null`.
  /// @throws AssertionError if the actual `Spliterator` does not have the expected characteristics.
  public SELF hasCharacteristics(int... characteristics) {
    return executeAssertion(() -> {
      isNotNull();
      Set<String> actualCharacteristics = characteristicNames(actual.characteristics());
      Set<String> expectedCharacteristics = characteristicNames(characteristics);
      if (actualCharacteristics.isEmpty() && expectedCharacteristics.isEmpty()) return;
      if (expectedCharacteristics.isEmpty()) throwAssertionError(actualIsNotEmpty(actualCharacteristics));
      Set<String> notFound = expectedCharacteristics.stream()
                                                    .filter(not(actualCharacteristics::contains))
                                                    .collect(toCollection(LinkedHashSet::new));
      if (!notFound.isEmpty())
        throwAssertionError(shouldContain(actualCharacteristics, expectedCharacteristics.toArray(new String[0]), notFound,
                                          CHARACTERISTICS_GROUP_DESCRIPTION));
    });
  }

  /// Asserts the actual [Spliterator] has only the given characteristics and nothing else.
  ///
  /// Example:
  /// ```java
  /// Spliterator<Integer> spliterator = Stream.of(1, 2, 3).spliterator();
  ///
  /// // this assertion succeeds:
  /// assertThat(spliterator).hasOnlyCharacteristics(Spliterator.SIZED,
  ///                                                Spliterator.SUBSIZED,
  ///                                                Spliterator.IMMUTABLE,
  ///                                                Spliterator.ORDERED);
  ///
  /// // this assertion fails as the Spliterator has additional characteristics IMMUTABLE and ORDERED:
  /// assertThat(spliterator).hasOnlyCharacteristics(Spliterator.SIZED, Spliterator.SUBSIZED);
  /// ```
  ///
  /// @param characteristics the expected characteristics.
  /// @return `this` assertion object.
  /// @throws AssertionError if the actual `Spliterator` is `null`.
  /// @throws AssertionError if the actual `Spliterator` does not have the expected characteristics
  ///           or the actual `Spliterator` has additional characteristics.
  public SELF hasOnlyCharacteristics(int... characteristics) {
    return executeAssertion(() -> {
      isNotNull();
      Set<String> actualCharacteristics = characteristicNames(actual.characteristics());
      Set<String> expectedCharacteristics = characteristicNames(characteristics);
      List<String> missing = new ArrayList<>(expectedCharacteristics);
      missing.removeAll(actualCharacteristics);
      List<String> unexpected = new ArrayList<>(actualCharacteristics);
      unexpected.removeAll(expectedCharacteristics);
      if (!unexpected.isEmpty() || !missing.isEmpty())
        throwAssertionError(shouldContainOnly(actualCharacteristics, expectedCharacteristics.toArray(new String[0]), missing,
                                              unexpected, CHARACTERISTICS_GROUP_DESCRIPTION));
    });
  }

  private static Set<String> characteristicNames(int... characteristics) {
    int combinedCharacteristics = 0;
    for (int characteristic : characteristics) {
      combinedCharacteristics |= characteristic;
    }
    return characteristicNames(combinedCharacteristics);
  }

  // names are kept in alphabetical order to get deterministic error messages
  private static Set<String> characteristicNames(int characteristics) {
    Set<String> names = new LinkedHashSet<>();
    if (hasCharacteristic(characteristics, Spliterator.CONCURRENT)) names.add("CONCURRENT");
    if (hasCharacteristic(characteristics, Spliterator.DISTINCT)) names.add("DISTINCT");
    if (hasCharacteristic(characteristics, Spliterator.IMMUTABLE)) names.add("IMMUTABLE");
    if (hasCharacteristic(characteristics, Spliterator.NONNULL)) names.add("NONNULL");
    if (hasCharacteristic(characteristics, Spliterator.ORDERED)) names.add("ORDERED");
    if (hasCharacteristic(characteristics, Spliterator.SIZED)) names.add("SIZED");
    if (hasCharacteristic(characteristics, Spliterator.SORTED)) names.add("SORTED");
    if (hasCharacteristic(characteristics, Spliterator.SUBSIZED)) names.add("SUBSIZED");
    return names;
  }

  private static boolean hasCharacteristic(int characteristics, int expectedCharacteristic) {
    return (characteristics & expectedCharacteristic) == expectedCharacteristic;
  }
}
