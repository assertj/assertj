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
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Int2DArrays;


/**
 * Base class for {@link Int2DArrayAssert} tests.
 * 
 * @author Maciej Wajcht
 */
public abstract class Int2DArrayAssertBaseTest extends BaseTestTemplate<Int2DArrayAssert, int[][]> {
  protected Int2DArrays arrays;

  @Override
  protected Int2DArrayAssert create_assertions() {
    return new Int2DArrayAssert(new int[][] {});
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    arrays = mock(Int2DArrays.class);
    assertions.int2dArrays = arrays;
  }
  
  protected Int2DArrays getArrays(Int2DArrayAssert someAssertions) {
    return someAssertions.int2dArrays;
  }
}
