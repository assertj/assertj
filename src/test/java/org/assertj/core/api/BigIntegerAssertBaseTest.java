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
package org.assertj.core.api;

import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.Comparables;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static org.mockito.Mockito.mock;


public abstract class BigIntegerAssertBaseTest extends ComparableAssertBaseTest<BigIntegerAssert, BigInteger> {

  protected static final String ONE_AS_STRING = "1";

  protected BigIntegers bigIntegers;

  @Override
  protected BigIntegerAssert create_assertions() {
    return new BigIntegerAssert(ONE);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    bigIntegers = mock(BigIntegers.class);
    assertions.bigIntegers = bigIntegers;
  }

  @Override
  protected Comparables getComparables(BigIntegerAssert someAssertions) {
    return someAssertions.bigIntegers;
  }
}
