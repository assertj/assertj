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
package org.assertj.core.api.bigdecimal;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.BigDecimalAssert;
import org.assertj.core.api.BigDecimalAssertBaseTest;


/**
 * Tests for <code>{@link BigDecimalAssert#isBetween(BigDecimal, BigDecimal)}</code>.
 * 
 * @author William Delanoue
 */
public class BigDecimalAssert_isBetween_Test extends BigDecimalAssertBaseTest {

  @Override
  protected BigDecimalAssert invoke_api_method() {
    return assertions.isBetween(new BigDecimal(6), new BigDecimal(8));
  }

  @Override
  protected void verify_internal_effects() {
    verify(bigDecimals).assertIsBetween(getInfo(assertions), getActual(assertions), new BigDecimal(6), new BigDecimal(8));
  }
}
