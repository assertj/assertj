/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.atomic.referencearray;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.testkit.NeverEqualComparator.NEVER_EQUALS_STRING;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class AtomicReferenceArrayAssert_usingComparatorForType_Test extends AtomicReferenceArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  private final Jedi actual = new Jedi("Yoda", "green");
  private final Jedi other = new Jedi("Luke", "blue");

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingComparatorForType(ALWAYS_EQUALS_STRING, String.class);
  }

  @Override
  protected void verify_internal_effects() {
    ObjectArrays arrays = getArrays(assertions);
    assertThat(arrays).isNotSameAs(arraysBefore);
    assertThat(arrays.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) arrays.getComparisonStrategy();
    assertThat(strategy.getComparator()).isInstanceOf(ExtendedByTypesComparator.class);
  }

  @Test
  void should_be_able_to_use_a_comparator_for_specified_types() {
    // GIVEN
    Object[] array = array("some", "other", new BigDecimal(42));
    AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(array);
    // THEN
    assertThat(atomicArray).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                           .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                           .contains("other", "any", new BigDecimal("42.0"));
  }

  @Test
  void should_use_comparator_for_type_when_using_element_comparator_ignoring_fields() {
    // GIVEN
    Object[] array = array(actual, "some");
    AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(array);
    // THEN
    assertThat(atomicArray).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                           .usingElementComparatorIgnoringFields("name")
                           .contains(other, "any");
  }

  @Test
  void should_use_comparator_for_type_when_using_element_comparator_on_fields() {
    // GIVEN
    Object[] array = array(actual, "some");
    AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(array);
    // THEN
    assertThat(atomicArray).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                           .usingElementComparatorOnFields("name", "lightSaberColor")
                           .contains(other, "any");
  }

  @Test
  void should_use_comparator_for_type_when_using_field_by_field_element_comparator() {
    // GIVEN
    Object[] array = array(actual, "some");
    AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(array);
    // THEN
    assertThat(atomicArray).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                           .usingFieldByFieldElementComparator()
                           .contains(other, "any");
  }

  @Test
  void should_not_use_comparator_on_fields_level_for_elements() {
    // GIVEN
    Object[] array = array(actual, "some");
    AtomicReferenceArray<Object> atomicArray = new AtomicReferenceArray<>(array);
    // WHEN
    AssertionError error = expectAssertionError(() -> {
      assertThat(atomicArray).usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_STRING, String.class)
                             .usingFieldByFieldElementComparator()
                             .contains(other, "any");
    });
    // THEN
    then(error).hasMessage(format("%nExpecting Object[]:%n"
                                  + "  [Yoda the Jedi, \"some\"]%n"
                                  + "to contain:%n"
                                  + "  [Luke the Jedi, \"any\"]%n"
                                  + "but could not find the following object(s):%n"
                                  + "  [\"any\"]%n"
                                  + "when comparing values using field/property by field/property comparator on all fields/properties%n"
                                  + "Comparators used:%n"
                                  + "- for elements fields (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], String -> AlwaysEqualComparator, Path -> lexicographic comparator (Path natural order)}%n"
                                  + "- for elements (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], Path -> lexicographic comparator (Path natural order)}"));
  }

  @Test
  void should_use_comparator_set_last_on_elements() {
    // GIVEN
    AtomicReferenceArray<Jedi> atomicArray = atomicArrayOf(actual, actual);
    // THEN
    assertThat(atomicArray).usingComparatorForElementFieldsWithType(NEVER_EQUALS_STRING, String.class)
                           .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                           .usingFieldByFieldElementComparator()
                           .contains(other, other);
  }

  @Test
  void should_be_able_to_replace_a_registered_comparator_by_type() {
    assertThat(asList(actual, actual)).usingComparatorForType(NEVER_EQUALS_STRING, String.class)
                                      .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                      .usingFieldByFieldElementComparator()
                                      .contains(other, other);
  }

  @Test
  void should_be_able_to_replace_a_registered_comparator_by_field() {
    // @format:off
    assertThat(asList(actual, actual)).usingComparatorForElementFieldsWithNames(NEVER_EQUALS_STRING, "name", "lightSaberColor")
                                      .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING, "name", "lightSaberColor")
                                      .usingFieldByFieldElementComparator()
                                      .contains(other, other);
    // @format:on
  }

  @Test
  void should_fail_because_of_comparator_set_last() {
    // GIVEN
    AtomicReferenceArray<Jedi> atomicArray = atomicArrayOf(actual, actual);
    // WHEN
    AssertionError error = expectAssertionError(() -> {
      assertThat(atomicArray).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                             .usingComparatorForElementFieldsWithType(NEVER_EQUALS_STRING, String.class)
                             .usingFieldByFieldElementComparator()
                             .contains(other, other);
    });
    // THEN
    then(error).hasMessage(format("%nExpecting Object[]:%n"
                                  + "  [Yoda the Jedi, Yoda the Jedi]%n"
                                  + "to contain:%n"
                                  + "  [Luke the Jedi, Luke the Jedi]%n"
                                  + "but could not find the following object(s):%n"
                                  + "  [Luke the Jedi]%n"
                                  + "when comparing values using field/property by field/property comparator on all fields/properties%n"
                                  + "Comparators used:%n"
                                  + "- for elements fields (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], String -> org.assertj.core.testkit.NeverEqualComparator, Path -> lexicographic comparator (Path natural order)}%n"
                                  + "- for elements (by type): {Double -> DoubleComparator[precision=1.0E-15], Float -> FloatComparator[precision=1.0E-6], String -> AlwaysEqualComparator, Path -> lexicographic comparator (Path natural order)}"));
  }
}
