/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

public class AtomicReferenceArrayAssert_usingComparatorForType_Test extends AtomicReferenceArrayAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

  private ObjectArrays arraysBefore;

  private Jedi actual = new Jedi("Yoda", "green");
  private Jedi other = new Jedi("Luke", "blue");

  @Before
  public void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingComparatorForType(ALWAY_EQUALS_STRING, String.class);
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
  public void should_be_able_to_use_a_comparator_for_specified_types() {
    assertThat(atomicArrayOf("some", "other", new BigDecimal(42)))
      .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
      .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
      .contains("other", "any", new BigDecimal("42.0"));
  }

  @Test
  public void should_use_comparator_for_type_when_using_element_comparator_ignoring_fields() {
    assertThat(atomicArrayOf(actual, "some")).usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
      .usingElementComparatorIgnoringFields("name")
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_element_comparator_on_fields() {
    assertThat(atomicArrayOf(actual, "some")).usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
      .usingElementComparatorOnFields("name", "lightSaberColor")
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_field_by_field_element_comparator() {
    assertThat(atomicArrayOf(actual, "some")).usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
      .usingFieldByFieldElementComparator()
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_recursive_field_by_field_element_comparator() {
    assertThat(atomicArrayOf(actual, "some")).usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
      .usingRecursiveFieldByFieldElementComparator()
      .contains(other, "any");
  }

  @Test
  public void should_not_use_comparator_on_fields_level_for_elements() {
    thrown.expectAssertionError("%nExpecting:%n" +
      " <[Yoda the Jedi, \"some\"]>%n" +
      "to contain:%n" +
      " <[Luke the Jedi, \"any\"]>%n" +
      "but could not find:%n" +
      " <[\"any\"]>%n" +
      "when comparing values using 'field/property by field/property comparator on all fields/properties'");

    assertThat(atomicArrayOf(actual, "some")).usingComparatorForElementFieldsWithType(ALWAY_EQUALS_STRING, String.class)
      .usingFieldByFieldElementComparator()
      .contains(other, "any");
  }
}
