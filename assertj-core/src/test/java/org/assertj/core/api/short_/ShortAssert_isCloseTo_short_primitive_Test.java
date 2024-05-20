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
package org.assertj.core.api.short_;

import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ShortAssert;
import org.assertj.core.api.ShortAssertBaseTest;
import org.assertj.core.data.Offset;

/**
 * Tests for <code>{@link ShortAssert#isCloseTo(short, Offset)}</code>.
 *
 * @author Sára Juhošová
 */
class ShortAssert_isCloseTo_short_primitive_Test extends ShortAssertBaseTest {

  private final Offset<Short> offset = offset((short) 1);
  private final short value = 16;

  @Override
  protected ShortAssert invoke_api_method() {
    return assertions.isCloseTo(value, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(shorts).assertIsCloseTo(getInfo(assertions), getActual(assertions), value, offset);
  }
}
