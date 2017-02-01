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

import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Base class for {@link BigDecimals} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link BigDecimals#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class BigDecimalsBaseTest extends NumbersBaseTest<BigDecimals, BigDecimal> {

  protected static final BigDecimal ONE_WITH_3_DECIMALS = new BigDecimal("1.000");

  @Override
  protected BigDecimals getNumbers() {
    return new BigDecimals();
  }

  @Override
  protected BigDecimals getNumbers(ComparisonStrategy comparisonStrategy) {
    return new BigDecimals(comparisonStrategy);
  }

  @Override
  protected Comparator<BigDecimal> getComparator() {
    return BIG_DECIMAL_COMPARATOR;
  }
}
