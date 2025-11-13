/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_usingComparatorForType_Test extends ObjectArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
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
    // THEN
    then(array).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
               .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
               .contains("other", "any", new BigDecimal("42.0"))
               .containsOnly("other", "any", new BigDecimal("42.00"))
               .containsExactly("other", "any", new BigDecimal("42.000"));
  }

}
