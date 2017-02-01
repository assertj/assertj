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

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.assertj.core.data.Offset;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link DoubleAssert#isEqualTo(double, Offset)}</code>.
 * 
 * @author Alex Ruiz
 */
public class DoubleAssert_isEqualTo_double_with_offset_Test extends DoubleAssertBaseTest {
  private final Offset<Double> offset = offset(5d);

  @Override
  protected DoubleAssert invoke_api_method() {
    return assertions.isEqualTo(8d, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(doubles).assertEqual(getInfo(assertions), getActual(assertions), 8d, offset);
  }
}
