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
package org.assertj.core.api.biginteger;

import org.assertj.core.api.BigIntegerAssert;
import org.assertj.core.api.BigIntegerAssertBaseTest;
import org.assertj.core.data.Percentage;

import java.math.BigInteger;

import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.verify;


public class BigIntegerAssert_isCloseToPercentage_Test extends BigIntegerAssertBaseTest {

  private final Percentage percentage = withPercentage(5);
  private final BigInteger value = BigInteger.TEN;

  @Override
  protected BigIntegerAssert invoke_api_method() {
    return assertions.isCloseTo(value, percentage);
  }

  @Override
  protected void verify_internal_effects() {
    verify(bigIntegers).assertIsCloseToPercentage(getInfo(assertions), getActual(assertions), value, percentage);
  }
}
