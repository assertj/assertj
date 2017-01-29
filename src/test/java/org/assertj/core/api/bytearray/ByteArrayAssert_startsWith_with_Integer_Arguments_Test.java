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


import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.test.IntArrays;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link ByteArrayAssert#startsWith(byte...)}</code>.
 */
public class ByteArrayAssert_startsWith_with_Integer_Arguments_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.startsWith(6, 8);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertStartsWith(getInfo(assertions), getActual(assertions), IntArrays.arrayOf(6, 8));
  }

  @Test
  public void invoke_api_like_user() {
    assertThat(new byte[] { 1, 2, 3}).startsWith(1, 2);
  }
}
