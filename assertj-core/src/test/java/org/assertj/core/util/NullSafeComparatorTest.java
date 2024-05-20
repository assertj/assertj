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

@DisplayName("NullSafeComparator")
class NullSafeComparatorTest {

  private static final NullSafeComparator<Object> NULL_SAFE_COMPARATOR = new NullSafeComparator<Object>() {

    @Override
    protected int compareNonNull(Object o1, Object o2) {
      return 0;
    }

  };

  @Test
  public void should_evaluate_null_instance_as_less_than_non_null_instance() {
    // GIVEN
    Object o1 = null;
    Object o2 = "foo";
    // WHEN
    int compare = NULL_SAFE_COMPARATOR.compare(o1, o2);
    // THEN
    then(compare).isNegative();
  }

  @Test
  public void should_evaluate_non_null_instance_as_greater_than_null_instance() {
    // GIVEN
    Object o1 = "foo";
    Object o2 = null;
    // WHEN
    int compare = NULL_SAFE_COMPARATOR.compare(o1, o2);
    // THEN
    then(compare).isPositive();
  }

  @Test
  public void should_evaluate_null_instances_as_equal() {
    // GIVEN
    Object o1 = null;
    Object o2 = null;
    // WHEN
    int compare = NULL_SAFE_COMPARATOR.compare(o1, o2);
    // THEN
    then(compare).isZero();
  }

}
