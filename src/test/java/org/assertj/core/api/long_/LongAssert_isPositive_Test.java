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
package org.assertj.core.api.long_;

import org.assertj.core.api.LongAssert;
import org.assertj.core.api.LongAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link LongAssert#isPositive()}</code>.
 * 
 * @author Alex Ruiz
 */
public class LongAssert_isPositive_Test extends LongAssertBaseTest {

  @Override
  protected LongAssert invoke_api_method() {
    return assertions.isPositive();
  }

  @Override
  protected void verify_internal_effects() {
    verify(longs).assertIsPositive(getInfo(assertions), getActual(assertions));
  }
}
