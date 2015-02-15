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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.integer_;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link IntegerAssert#isBetween(Integer, Integer)}</code>.
 * 
 * @author William Delanoue
 */
public class IntegerAssert_isBetween_Integers_Test extends IntegerAssertBaseTest {

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.isBetween(6, 8);
  }

  @Override
  protected void verify_internal_effects() {
    verify(integers).assertIsBetween(getInfo(assertions), getActual(assertions), 6, 8);
  }
}
