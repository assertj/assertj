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
package org.assertj.core.api.shortarray;

import static org.assertj.core.test.TestData.someIndex;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.assertj.core.data.Index;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link ShortArrayAssert#doesNotContain(short, Index)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ShortArrayAssert_doesNotContain_at_Index_Test extends ShortArrayAssertBaseTest {

  private final Index index = someIndex();

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.doesNotContain((short) 8, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), (short) 8, index);
  }
}
