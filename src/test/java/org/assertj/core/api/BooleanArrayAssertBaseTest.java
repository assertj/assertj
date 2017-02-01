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
package org.assertj.core.api;

import static org.assertj.core.test.BooleanArrays.emptyArray;
import static org.mockito.Mockito.mock;

import org.assertj.core.internal.BooleanArrays;


/**
 * Base class for {@link BooleanArrayAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class BooleanArrayAssertBaseTest extends BaseTestTemplate<BooleanArrayAssert, boolean[]> {
  protected BooleanArrays arrays;

  @Override
  protected BooleanArrayAssert create_assertions() {
    return new BooleanArrayAssert(emptyArray());
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    arrays = mock(BooleanArrays.class);
    assertions.arrays = arrays;
  }
  
  protected BooleanArrays getArrays(BooleanArrayAssert someAssertions) {
    return someAssertions.arrays;
  }
}
