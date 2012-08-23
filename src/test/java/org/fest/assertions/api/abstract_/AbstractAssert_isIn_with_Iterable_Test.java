/*
 * Created on Feb 3, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api.abstract_;

import static org.fest.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.fest.assertions.api.AbstractAssertBaseTest;
import org.fest.assertions.api.ConcreteAssert;
import org.junit.BeforeClass;

/**
 * Tests for <code>{@link AbstractAssert#isIn(Iterable))}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 * @author Nicolas François
 */
public class AbstractAssert_isIn_with_Iterable_Test extends AbstractAssertBaseTest {

  private static Iterable<?> values;

  @BeforeClass
  public static void setUpOnce() {
    values = newArrayList("Yoda", "Luke");
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
