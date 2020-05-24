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
package org.assertj.core.api.double2darray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Double2DArrayAssert;
import org.assertj.core.api.Double2DArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;


/**
 * Tests for <code>{@link Double2DArrayAssert#hasSize(int, int)}</code>.
 * 
 * @author Maciej Wajcht
 */
@DisplayName("Double2DArrayAssert hasSize")
class Double2DArrayAssert_hasSize_Test extends Double2DArrayAssertBaseTest {

  @Override
  protected Double2DArrayAssert invoke_api_method() {
    return assertions.hasSize(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasSize(getInfo(assertions), getActual(assertions), 1, 2);
  }
}
