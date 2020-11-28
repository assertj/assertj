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
package org.assertj.core.api.long2darray;

import static org.assertj.core.test.TestData.someIndex;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Long2DArrayAssert;
import org.assertj.core.api.Long2DArrayAssertBaseTest;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link Long2DArrayAssert#contains(long[], Index)}</code>.
 * 
 * @author Maciej Wajcht
 */
@DisplayName("Long2DArrayAssert contains")
class Long2DArrayAssert_contains_at_Index_Test extends Long2DArrayAssertBaseTest {
  private final Index index = someIndex();

  @Override
  protected Long2DArrayAssert invoke_api_method() {
    return assertions.contains(new long[] { 8L, 9L }, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContains(getInfo(assertions), getActual(assertions), new long[] { 8L, 9L }, index);
  }
}
