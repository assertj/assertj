/*
 * Created on Feb 5, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link AbstractAssert#isNotIn(Iterable)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 * @author Nicolas François
 */
public class AbstractAssert_isNotIn_with_vararg_Test extends AbstractAssertBaseTest {

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isNotIn("Yoda", "Luke");
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsNotIn(getInfo(assertions), getActual(assertions), new Object[] { "Yoda", "Luke" });
  }
}
