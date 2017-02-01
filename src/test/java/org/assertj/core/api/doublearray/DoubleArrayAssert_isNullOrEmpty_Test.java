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
package org.assertj.core.api.doublearray;

import static org.mockito.Mockito.verify;


import org.assertj.core.api.DoubleArrayAssert;
import org.assertj.core.api.DoubleArrayAssertBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link DoubleArrayAssert#isNullOrEmpty()}</code>.
 * 
 * @author Alex Ruiz
 */
public class DoubleArrayAssert_isNullOrEmpty_Test extends DoubleArrayAssertBaseTest {

  @Override
  protected DoubleArrayAssert invoke_api_method() {
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
    // Disable this test since isNullOrEmpty is void
  }
}
