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

import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;

public class ShortArrayAssert_containsAnyOf_Test extends ShortArrayAssertBaseTest {

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.containsAnyOf((short) 1, (short) 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsAnyOf(getInfo(assertions), getActual(assertions), arrayOf((short) 1, (short) 2));
  }

}
