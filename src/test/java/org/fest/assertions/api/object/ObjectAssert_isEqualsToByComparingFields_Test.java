/*
 * 
 * Created on May 27, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.api.object;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.ObjectAssert;
import org.fest.assertions.api.ObjectAssertBaseTest;
import org.fest.test.Jedi;

/**
 * Tests for <code>{@link ObjectAssert#isEqualsToByComparingFields(Object)}</code>.
 * 
 * @author Nicolas François
 */
public class ObjectAssert_isEqualsToByComparingFields_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Blue");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.isEqualsToByComparingFields(other);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsLenientEqualsToByIgnoringFields(getInfo(assertions), getActual(assertions), other);
  }
}
