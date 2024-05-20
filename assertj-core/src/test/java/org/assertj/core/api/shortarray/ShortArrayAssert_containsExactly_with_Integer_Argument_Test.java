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
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.ShortArrayAssert#containsExactly(int...)}</code>.
 * 
 * @author Dan Avila
 */
@DisplayName("ShortArrayAssert containsExactly (ints)")
class ShortArrayAssert_containsExactly_with_Integer_Argument_Test extends ShortArrayAssertNullTest {

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.containsExactly(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactly(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

  @Test
  void should_honor_the_given_element_comparator() {
    short[] actual = new short[] { (short) 1, (short) 2, (short) 3 };
    assertThat(actual).usingElementComparator(new AbsValueComparator<Short>())
                      .containsExactly(-1, 2, 3);
  }

  @Override
  protected void invoke_api_with_null_value(AbstractShortArrayAssert<?> emptyAssert, int[] nullArray) {
    emptyAssert.containsExactly(nullArray);
  }
}
