/*
 * Created on Oct 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.long_;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.LongAssert;
import org.fest.assertions.api.LongAssertBaseTest;

/**
 * Tests for <code>{@link LongAssert#isNotEqualTo(long)}</code>.
 * 
 * @author Alex Ruiz
 */
public class LongAssert_isNotEqualTo_long_Test extends LongAssertBaseTest {

  @Override
  protected LongAssert invoke_api_method() {
    return assertions.isNotEqualTo(8L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(longs).assertNotEqual(getInfo(assertions), getActual(assertions), 8L);
  }
}
