/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.ByteArrays.arrayOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.ByteArrayAssert#containsExactly(byte...)}</code>.
 *
 * @author Jean-Christophe Gay
 */
class ByteArrayAssert_containsExactly_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.containsExactly((byte) 1, (byte) 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

  @Test
  void invoke_api_like_user() {
    // GIVEN
    byte[] actual = new byte[] { 1, 2, 3 };
    // WHEN/THEN
    assertThat(actual).containsExactly((byte) 1, (byte) 2, (byte) 3);
  }

  @Test
  void does_not_honor_the_given_element_comparator() {
    // GIVEN
    byte[] actual = new byte[] { 1, 2, 3 };
    // WHEN/THEN
    expectAssertionError(() -> assertThat(actual).usingElementComparator(new AbsValueComparator<Byte>())
                                                 .containsExactly((byte) -1, (byte) 2, (byte) 3));
  }
}
