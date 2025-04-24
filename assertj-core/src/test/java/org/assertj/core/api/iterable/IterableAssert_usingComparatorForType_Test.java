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
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.testkit.NeverEqualComparator.NEVER_EQUALS_STRING;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;
import static org.assertj.core.util.Lists.list;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
@DisplayName("IterableAssert usingComparatorForType")
class IterableAssert_usingComparatorForType_Test extends IterableAssertBaseTest {

  private Jedi actual = new Jedi("Yoda", "green");
  private Jedi other = new Jedi("Luke", "blue");

  private Iterables iterablesBefore;

  @BeforeEach
  void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingComparatorForType(ALWAYS_EQUALS_STRING, String.class);
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
  void should_be_able_to_use_a_comparator_for_specified_types() {
    // GIVEN
    List<Object> list = asList("some", "other", new BigDecimal(42));
    // THEN
    assertThat(list).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                    .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                    .contains("other", "any", new BigDecimal("42.0"))
                    .containsOnly("other", "any", new BigDecimal("42.00"))
                    .containsExactly("other", "any", new BigDecimal("42.000"));
  }

  @Test
  void should_be_able_to_replace_a_registered_comparator_by_type() {
    assertThat(list("foo", "bar")).usingComparatorForType(NEVER_EQUALS_STRING, String.class)
                                  .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                  .contains("baz");
  }

}
