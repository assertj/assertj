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

import org.assertj.core.internal.Byte2DArrays;


/**
 * Base class for {@link Byte2DArrayAssert} tests.
 * 
 * @author Maciej Wajcht
 */
public abstract class Byte2DArrayAssertBaseTest extends BaseTestTemplate<Byte2DArrayAssert, byte[][]> {
  protected Byte2DArrays arrays;

  @Override
  protected Byte2DArrayAssert create_assertions() {
    return new Byte2DArrayAssert(new byte[][] {});
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    arrays = mock(Byte2DArrays.class);
    assertions.byte2dArrays = arrays;
  }
  
  protected Byte2DArrays getArrays(Byte2DArrayAssert someAssertions) {
    return someAssertions.byte2dArrays;
  }
}
