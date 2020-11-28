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
package org.assertj.core.api.short2darray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Short2DArrayAssert;
import org.assertj.core.api.Short2DArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link Short2DArrayAssert#hasDimensions(int, int)}</code>.
 * 
 * @author Maciej Wajcht
 */
@DisplayName("Short2DArrayAssert hasDimensions")
class Short2DArrayAssert_hasDimensions_Test extends Short2DArrayAssertBaseTest {

  @Override
  protected Short2DArrayAssert invoke_api_method() {
    return assertions.hasDimensions(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasDimensions(getInfo(assertions), getActual(assertions), 1, 2);
  }
}
