/*
 * Created on Dec 17, 2010
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
package org.fest.assertions.api.bytearray;

import static org.fest.assertions.test.ByteArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.fest.assertions.api.ByteArrayAssert;
import org.fest.assertions.api.ByteArrayAssertBaseTest;

/**
 * Tests for <code>{@link ByteArrayAssert#doesNotContain(byte...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ByteArrayAssert_doesNotContain_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.doesNotContain((byte) 6, (byte) 8);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), arrayOf(6, 8));
  }
}
