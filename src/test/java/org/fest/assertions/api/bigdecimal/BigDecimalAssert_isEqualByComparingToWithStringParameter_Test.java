/*
 * Created on Feb 8, 2011
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
package org.fest.assertions.api.bigdecimal;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.fest.assertions.api.BigDecimalAssert;
import org.fest.assertions.api.BigDecimalAssertBaseTest;

/**
 * Tests for <code>{@link BigDecimalAssert#isEqualByComparingTo(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimalAssert_isEqualByComparingToWithStringParameter_Test extends BigDecimalAssertBaseTest {

  @Override
  protected BigDecimalAssert invoke_api_method() {
    return assertions.isEqualByComparingTo(ONE_AS_STRING);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertEqualByComparison(getInfo(assertions), getActual(assertions), new BigDecimal(ONE_AS_STRING));
  }
}
