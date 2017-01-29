/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.integer_;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;
import org.assertj.core.data.Offset;

import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link IntegerAssert#isNotCloseTo(Integer, Offset)}</code>.
 *
 * @author Chris Arnott
 */
public class IntegerAssert_isNotCloseTo_integer_Test extends IntegerAssertBaseTest {

  private final Offset<Integer> offset = offset(5);
  private final Integer value = 8;

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.isNotCloseTo(value, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(integers).assertIsNotCloseTo(getInfo(assertions), getActual(assertions), value, offset);
  }
}
