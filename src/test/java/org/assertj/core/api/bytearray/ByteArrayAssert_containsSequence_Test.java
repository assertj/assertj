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
package org.assertj.core.api.bytearray;

import static org.assertj.core.test.ByteArrays.arrayOf;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link ByteArrayAssert#containsSequence(byte...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ByteArrayAssert_containsSequence_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.containsSequence((byte) 6, (byte) 8);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsSequence(getInfo(assertions), getActual(assertions), arrayOf(6, 8));
  }
}
