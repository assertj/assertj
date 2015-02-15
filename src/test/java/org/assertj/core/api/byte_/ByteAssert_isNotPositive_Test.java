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
package org.assertj.core.api.byte_;

import org.assertj.core.api.ByteAssert;
import org.assertj.core.api.ByteAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link ByteAssert#isNotPositive()}</code>.
 * 
 * @author Nicolas François
 */
public class ByteAssert_isNotPositive_Test extends ByteAssertBaseTest {

  @Override
  protected ByteAssert invoke_api_method() {
    return assertions.isNotPositive();
  }

  @Override
  protected void verify_internal_effects() {
    verify(bytes).assertIsNotPositive(getInfo(assertions), getActual(assertions));
  }
}
