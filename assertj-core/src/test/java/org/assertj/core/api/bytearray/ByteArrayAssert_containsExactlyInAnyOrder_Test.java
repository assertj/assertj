/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.ByteArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.ByteArrayAssert#containsExactlyInAnyOrder(byte...)}</code>.
 */
class ByteArrayAssert_containsExactlyInAnyOrder_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.containsExactlyInAnyOrder((byte) 1, (byte) 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactlyInAnyOrder(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

  @Test
  void invoke_api_like_user() {
    assertThat(new byte[] { 1, 2, 2 }).containsExactlyInAnyOrder((byte) 2, (byte) 2, (byte) 1);
  }
}
