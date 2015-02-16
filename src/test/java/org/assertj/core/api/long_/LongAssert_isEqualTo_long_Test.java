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
package org.assertj.core.api.long_;

import org.assertj.core.api.LongAssert;
import org.assertj.core.api.LongAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link LongAssert#isEqualTo(long)}</code>.
 * 
 * @author Alex Ruiz
 */
public class LongAssert_isEqualTo_long_Test extends LongAssertBaseTest {

  @Override
  protected LongAssert invoke_api_method() {
    return assertions.isEqualTo(8L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(longs).assertEqual(getInfo(assertions), getActual(assertions), 8L);
  }
}
