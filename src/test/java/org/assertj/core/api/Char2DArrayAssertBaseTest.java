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

import org.assertj.core.internal.Char2DArrays;


/**
 * Base class for {@link Char2DArrayAssert} tests.
 *
 * @author Maciej Wajcht
 */
public abstract class Char2DArrayAssertBaseTest extends BaseTestTemplate<Char2DArrayAssert, char[][]> {
  protected Char2DArrays arrays;

  @Override
  protected Char2DArrayAssert create_assertions() {
    return new Char2DArrayAssert(new char[][] {});
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    arrays = mock(Char2DArrays.class);
    assertions.char2dArrays = arrays;
  }
  
  protected Char2DArrays getArrays(Char2DArrayAssert someAssertions) {
    return someAssertions.char2dArrays;
  }
}
