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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;

import java.util.Comparator;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtomicReferenceArrayAssert_usingElementComparatorIgnoringFields_Test
    extends AtomicReferenceArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @BeforeEach
  public void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.usingElementComparatorIgnoringFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    ObjectArrays iterables = getArrays(assertions);
    assertThat(iterables).isNotSameAs(arraysBefore);
    assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy();
    assertThat(strategy.getComparator()).isInstanceOf(ExtendedByTypesComparator.class);
    assertThat(((IgnoringFieldsComparator) ((ExtendedByTypesComparator) strategy.getComparator())
      .getComparator()).getFields()).containsOnly("field");
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields_of_elements_when_using_element_comparator_ignoring_fields() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingElementComparatorIgnoringFields("lightSaberColor")
                                     .contains(other);
  }

  @Test
  public void comparators_for_element_field_names_should_have_precedence_over_comparators_for_element_field_types_using_element_comparator_ignoring_fields() {
    Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingComparatorForElementFieldsWithType(comparator, String.class)
                                     .usingElementComparatorIgnoringFields("lightSaberColor")
                                     .contains(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_element_fields_with_specified_type_using_element_comparator_ignoring_fields() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "blue");

    assertThat(atomicArrayOf(actual)).usingComparatorForElementFieldsWithType(ALWAY_EQUALS_STRING, String.class)
                                     .usingElementComparatorIgnoringFields("name")
                                     .contains(other);
  }

}
