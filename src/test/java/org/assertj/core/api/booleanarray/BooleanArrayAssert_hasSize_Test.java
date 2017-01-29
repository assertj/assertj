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
package org.assertj.core.api.booleanarray;

import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link BooleanArrayAssert#hasSize(int)}</code>.
 * 
 * @author Alex Ruiz
 */
public class BooleanArrayAssert_hasSize_Test extends BooleanArrayAssertBaseTest {

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    return assertions.hasSize(6);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasSize(getInfo(assertions), getActual(assertions), 6);
  }
}
