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

import org.assertj.core.internal.Boolean2DArrays;


/**
 * Base class for {@link Boolean2DArrayAssert} tests.
 * 
 * @author Maciej Wajcht
 */
public abstract class Boolean2DArrayAssertBaseTest extends BaseTestTemplate<Boolean2DArrayAssert, boolean[][]> {
  protected Boolean2DArrays arrays;

  @Override
  protected Boolean2DArrayAssert create_assertions() {
    return new Boolean2DArrayAssert(new boolean[][] {});
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    arrays = mock(Boolean2DArrays.class);
    assertions.boolean2dArrays = arrays;
  }
  
  protected Boolean2DArrays getArrays(Boolean2DArrayAssert someAssertions) {
    return someAssertions.boolean2dArrays;
  }
}
