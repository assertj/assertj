/*
 * Created on Jun 3, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.api.abstract_;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.AbstractAssertBaseTest;
import org.fest.assertions.api.ConcreteAssert;
import org.fest.assertions.api.ObjectAssert;

/**
 * Tests for <code>{@link ObjectAssert#isNotInstanceOf(Class)}</code>.
 * 
 * @author Nicolas François
 */
public class AbstractAssert_isNotInstanceOf_Test extends AbstractAssertBaseTest {

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isNotInstanceOf(String.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsNotInstanceOf(getInfo(assertions), getActual(assertions), String.class);
  }
}
