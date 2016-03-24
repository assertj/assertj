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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;


import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.AbsValueComparator;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for {@link BigDecimals} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link BigDecimals#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class BigDecimalsBaseTest {

  protected static final BigDecimal ONE_WITH_3_DECIMALS = new BigDecimal("1.000");

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected BigDecimals bigDecimals;

  protected ComparatorBasedComparisonStrategy comparatorComparisonStrategy;
  /**
   * {@link BigDecimals} using a comparison strategy based on {@link BigDecimalComparator}.
   */
  protected BigDecimals bigDecimalsWithComparatorComparisonStrategy;
  // another BigDecimals with a custom ComparisonStrategy other than bigDecimalsWithComparatorComparisonStrategy
  protected BigDecimals bigDecimalsWithAbsValueComparisonStrategy;
  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    bigDecimals = new BigDecimals();
    bigDecimals.setFailures(failures);
    comparatorComparisonStrategy = new ComparatorBasedComparisonStrategy(BIG_DECIMAL_COMPARATOR);
    bigDecimalsWithComparatorComparisonStrategy = new BigDecimals(comparatorComparisonStrategy);
    bigDecimalsWithComparatorComparisonStrategy.failures = failures;
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<BigDecimal>());
    bigDecimalsWithAbsValueComparisonStrategy = new BigDecimals(absValueComparisonStrategy);
    bigDecimalsWithAbsValueComparisonStrategy.failures = failures;
  }
}