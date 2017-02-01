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

import java.math.BigInteger;

import static org.mockito.Mockito.verify;

public class BigIntegerAssert_isBetween_Test extends BigIntegerAssertBaseTest {

  @Override
  protected BigIntegerAssert invoke_api_method() {
    return assertions.isBetween(new BigInteger("6"), new BigInteger("8"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(bigIntegers).assertIsBetween(getInfo(assertions), getActual(assertions), new BigInteger("6"), new BigInteger("8"));
  }
}
