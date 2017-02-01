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
package org.assertj.core.api.abstract_;

import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;


import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;
import org.junit.BeforeClass;

/**
 * Tests for <code>{@link AbstractAssert#isIn(Object...)}</code>.
 * 
 * @author Yvonne Wang
 */
public class AbstractAssert_isIn_with_array_Test extends AbstractAssertBaseTest {

  private static Object[] values;

  @BeforeClass
  public static void setUpOnce() {
    values = array("Yoda", "Luke");
  }

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isIn(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsIn(getInfo(assertions), getActual(assertions), values);
  }
}
