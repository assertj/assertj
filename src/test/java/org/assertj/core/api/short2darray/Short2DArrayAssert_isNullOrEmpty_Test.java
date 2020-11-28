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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.short2darray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Short2DArrayAssert;
import org.assertj.core.api.Short2DArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Short2DArrayAssert#isNullOrEmpty()}</code>.
 * 
 * @author Maciej Wajcht
 */
@DisplayName("Short2DArrayAssert isNullOrEmpty")
class Short2DArrayAssert_isNullOrEmpty_Test extends Short2DArrayAssertBaseTest {

  @Override
  protected Short2DArrayAssert invoke_api_method() {
    assertions.isNullOrEmpty();
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertNullOrEmpty(getInfo(assertions), getActual(assertions));
  }

  @Override
  @Test
  public void should_return_this() {
    // Disable this test because isEmpty is void
  }
}
