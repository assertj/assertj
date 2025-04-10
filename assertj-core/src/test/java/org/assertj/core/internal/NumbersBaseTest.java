/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;

public abstract class NumbersBaseTest<NUMBERS_TYPE extends Numbers<?>, NUMBER_TYPE extends Number> {

  protected Failures failures;
  protected AssertionInfo info = someInfo();
  protected NUMBERS_TYPE numbers;

  protected ComparatorBasedComparisonStrategy comparatorComparisonStrategy;
  /**
   * {@link NUMBERS_TYPE} using a comparison strategy based on {@link org.assertj.core.util.AbstractComparableNumberComparator}.
   */
  protected NUMBERS_TYPE numbersWithComparatorComparisonStrategy;
  // another NUMBERS_TYPE with a custom ComparisonStrategy other than numbersWithComparatorComparisonStrategy
  protected NUMBERS_TYPE numbersWithAbsValueComparisonStrategy;
  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
    numbers = getNumbers();
    numbers.setFailures(failures);
    comparatorComparisonStrategy = new ComparatorBasedComparisonStrategy(getComparator());
    numbersWithComparatorComparisonStrategy = getNumbers(comparatorComparisonStrategy);
    numbersWithComparatorComparisonStrategy.failures = failures;
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<NUMBER_TYPE>());
    numbersWithAbsValueComparisonStrategy = getNumbers(absValueComparisonStrategy);
    numbersWithAbsValueComparisonStrategy.failures = failures;
  }

  protected abstract NUMBERS_TYPE getNumbers();

  protected abstract NUMBERS_TYPE getNumbers(ComparisonStrategy comparisonStrategy);

  protected abstract Comparator<NUMBER_TYPE> getComparator();
}
