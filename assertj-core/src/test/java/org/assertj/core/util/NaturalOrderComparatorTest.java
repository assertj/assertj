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
package org.assertj.core.util;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NaturalOrderComparator")
class NaturalOrderComparatorTest {

  private static final NaturalOrderComparator<String> NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator<>(String.class);

  @Test
  public void should_compare_instances_according_to_their_natural_order() {
    // GIVEN
    String s1 = "aaa";
    String s2 = "bbb";
    // WHEN
    int less = NATURAL_ORDER_COMPARATOR.compareNonNull(s1, s2);
    int equal = NATURAL_ORDER_COMPARATOR.compareNonNull(s1, s1);
    int greater = NATURAL_ORDER_COMPARATOR.compareNonNull(s2, s1);
    // THEN
    then(less).isNegative();
    then(equal).isZero();
    then(greater).isPositive();
  }

}
