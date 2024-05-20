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
package org.assertj.core.api.integer_;

import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;
import org.assertj.core.data.Offset;

/**
 * Tests for <code>{@link IntegerAssert#isNotCloseTo(int, Offset)}</code>.
 *
 * @author Sára Juhošová
 */
class IntegerAssert_isNotCloseTo_int_Test extends IntegerAssertBaseTest {

  private final Offset<Integer> offset = offset(13);
  private final int value = 7;

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.isNotCloseTo(value, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(integers).assertIsNotCloseTo(getInfo(assertions), getActual(assertions), value, offset);
  }
}
