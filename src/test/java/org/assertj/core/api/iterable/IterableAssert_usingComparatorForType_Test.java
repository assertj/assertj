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
package org.assertj.core.api.iterable;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualStringComparator.ALWAY_EQUALS;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

public class IterableAssert_usingComparatorForType_Test extends IterableAssertBaseTest {

  private Jedi actual = new Jedi("Yoda", "green");
  private Jedi other = new Jedi("Luke", "blue");

  private Iterables iterablesBefore;

  @Before
  public void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingComparatorForType(ALWAY_EQUALS, String.class);
  }

  @Override
  protected void verify_internal_effects() {
    Iterables iterables = getIterables(assertions);
    assertThat(iterables).isNotSameAs(iterablesBefore);
    assertThat(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    ComparatorBasedComparisonStrategy strategy = (ComparatorBasedComparisonStrategy) iterables.getComparisonStrategy();
    assertThat(strategy.getComparator()).isInstanceOf(ExtendedByTypesComparator.class);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_types() {
    assertThat(asList("some", "other", new BigDecimal(42)))
      .usingComparatorForType(ALWAY_EQUALS, String.class)
      .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
      .contains("other", "any", new BigDecimal("42.0"));
  }

  @Test
  public void should_use_comparator_for_type_when_using_element_comparator_ignoring_fields() {
    assertThat(asList(actual, "some")).usingComparatorForType(ALWAY_EQUALS, String.class)
      .usingElementComparatorIgnoringFields("name")
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_element_comparator_on_fields() {
    assertThat(asList(actual, "some")).usingComparatorForType(ALWAY_EQUALS, String.class)
      .usingElementComparatorOnFields("name", "lightSaberColor")
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_field_by_field_element_comparator() {
    assertThat(asList(actual, "some")).usingComparatorForType(ALWAY_EQUALS, String.class)
      .usingFieldByFieldElementComparator()
      .contains(other, "any");
  }

  @Test
  public void should_use_comparator_for_type_when_using_recursive_field_by_field_element_comparator() {
    assertThat(asList(actual, "some")).usingComparatorForType(ALWAY_EQUALS, String.class)
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
      "when comparing values using 'field/property by field/property comparator on all fields/properties except [\"name\"]'");

    assertThat(asList(actual, "some")).usingComparatorForElementFieldsWithType(ALWAY_EQUALS, String.class)
      .usingElementComparatorIgnoringFields("name")
      .contains(other, "any");
  }
}
