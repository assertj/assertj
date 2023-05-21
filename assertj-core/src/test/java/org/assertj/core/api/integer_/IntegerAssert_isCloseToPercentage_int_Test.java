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
package org.assertj.core.api.integer_;

import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;
import org.assertj.core.data.Percentage;

/**
 * Tests for <code>{@link IntegerAssert#isCloseTo(int, Percentage)}</code>.
 *
 * @author Sára Juhošová
 */
class IntegerAssert_isCloseToPercentage_int_Test extends IntegerAssertBaseTest {

  private final Percentage percentage = withPercentage(3);
  private final int value = 42;

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.isCloseTo(value, percentage);
  }

  @Override
  protected void verify_internal_effects() {
    verify(integers).assertIsCloseToPercentage(getInfo(assertions), getActual(assertions), value, percentage);
  }
}
