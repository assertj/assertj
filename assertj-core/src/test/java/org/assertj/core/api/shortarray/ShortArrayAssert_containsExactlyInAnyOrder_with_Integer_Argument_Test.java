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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.ShortArrayAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.ShortArrayAssert#containsExactlyInAnyOrder(int...)}</code>.
 * 
 * @author Dan Avila
 */
@DisplayName("ShortArrayAssert containsExactlyInAnyOrder (ints)")
class ShortArrayAssert_containsExactlyInAnyOrder_with_Integer_Argument_Test extends ShortArrayAssertNullTest {

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.containsExactlyInAnyOrder(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactlyInAnyOrder(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

  @Test
  void invoke_api_like_user() {
    assertThat(new short[] { 1, 2, 2 }).containsExactlyInAnyOrder(2, 2, 1);
  }

  @Override
  protected void invoke_api_with_null_value(AbstractShortArrayAssert<?> emptyAssert, int[] nullArray) {
    emptyAssert.containsExactlyInAnyOrder(nullArray);
  }
}
