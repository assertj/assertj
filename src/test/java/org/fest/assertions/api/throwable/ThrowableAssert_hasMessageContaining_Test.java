/*
 * Created on Dec 24, 2010
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
package org.fest.assertions.api.throwable;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.ThrowableAssert;
import org.fest.assertions.api.ThrowableAssertBaseTest;

/**
 * Tests for <code>{@link ThrowableAssert#hasMessageContaining(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ThrowableAssert_hasMessageContaining_Test extends ThrowableAssertBaseTest {

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasMessageContaining("able");
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasMessageContaining(getInfo(assertions), getActual(assertions), "able");
  }
}
