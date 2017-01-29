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
package org.assertj.core.internal;

import java.math.BigInteger;
import java.util.Comparator;

import static org.assertj.core.util.BigIntegerComparator.BIG_INTEGER_COMPARATOR;


public class BigIntegersBaseTest extends NumbersBaseTest<BigIntegers, BigInteger> {

  @Override
  protected BigIntegers getNumbers() {
    return new BigIntegers();
  }

  @Override
  protected BigIntegers getNumbers(ComparisonStrategy comparisonStrategy) {
    return new BigIntegers(comparisonStrategy);
  }

  @Override
  protected Comparator<BigInteger> getComparator() {
    return BIG_INTEGER_COMPARATOR;
  }

}
