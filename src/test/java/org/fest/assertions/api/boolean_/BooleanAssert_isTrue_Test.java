/*
 * Created on Oct 22, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.boolean_;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.BooleanAssert;
import org.fest.assertions.api.BooleanAssertBaseTest;

/**
 * Tests for <code>{@link BooleanAssert#isTrue()}</code>.
 * 
 * @author Alex Ruiz
 */
public class BooleanAssert_isTrue_Test extends BooleanAssertBaseTest {

  @Override
  protected BooleanAssert invoke_api_method() {
    return assertions.isTrue();
  }

  @Override
  protected void verify_internal_effects() {
    verify(booleans).assertEqual(getInfo(assertions), getActual(assertions), true);
  }
}
