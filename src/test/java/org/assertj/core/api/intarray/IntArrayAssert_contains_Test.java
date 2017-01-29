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
package org.assertj.core.api.intarray;

import static org.assertj.core.test.IntArrays.arrayOf;

import org.assertj.core.api.IntArrayAssert;
import org.assertj.core.api.IntArrayAssertBaseTest;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link IntArrayAssert#contains(int...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class IntArrayAssert_contains_Test extends IntArrayAssertBaseTest {

  @Override
  protected IntArrayAssert invoke_api_method() {
    return assertions.contains(6, 8);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContains(getInfo(assertions), getActual(assertions), arrayOf(6, 8));
  }
}
