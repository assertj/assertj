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
package org.assertj.core.api.double_;

import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.assertj.core.data.Offset;

public class DoubleAssert_isCloseTo_double_Test extends DoubleAssertBaseTest {

  private final Offset<Double> offset = offset(5.0);
  private final Double value = 8.0;

  @Override
  protected DoubleAssert invoke_api_method() {
    return assertions.isCloseTo(value, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(doubles).assertIsCloseTo(getInfo(assertions), getActual(assertions), value, offset);
  }
}
