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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.int2darray;

import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Int2DArrayAssert;
import org.assertj.core.api.Int2DArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link Int2DArrayAssert#hasSameDimensionsAs(Object)}</code>.
 *
 * @author Maciej Wajcht
 */
@DisplayName("Int2DArrayAssert hasSameDimensionsAs")
class Int2DArrayAssert_hasSameDimensionsAs_Test extends Int2DArrayAssertBaseTest {

  @Override
  protected Int2DArrayAssert invoke_api_method() {
    return assertions.hasSameDimensionsAs(array("a", "b"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasSameDimensionsAs(getInfo(assertions), getActual(assertions), array("a", "b"));
  }
}
