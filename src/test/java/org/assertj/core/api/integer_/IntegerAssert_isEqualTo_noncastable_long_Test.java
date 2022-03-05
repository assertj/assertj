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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.integer_;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;

import static org.mockito.Mockito.verify;

class IntegerAssert_isEqualTo_large_long_Test extends IntegerAssertBaseTest {

  private final long expected = ((long) Integer.MAX_VALUE) + 2L;

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.isEqualTo(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(integers).assertEqual(getInfo(assertions), getActual(assertions), expected);
  }
}
