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

import static org.assertj.core.testkit.LongArrays.arrayOf;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for testing <code>{@link LongArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and another
 * with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link LongArrays#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class LongArraysBaseTest {

  /**
   * is initialized with {@link #initActualArray()} with default value = {6, 8, 10}
   */
  protected long[] actual;
  protected Failures failures;
  protected LongArrays arrays;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected LongArrays arraysWithCustomComparisonStrategy;

  private AbsValueComparator<Long> absValueComparator = new AbsValueComparator<>();

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
    arrays = new LongArrays();
    arrays.failures = failures;
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    arraysWithCustomComparisonStrategy = new LongArrays(absValueComparisonStrategy);
    arraysWithCustomComparisonStrategy.failures = failures;
    initActualArray();
  }

  protected void initActualArray() {
    actual = arrayOf(6L, 8L, 10L);
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return absValueComparator;
  }

  protected void setArrays(Arrays internalArrays) {
    arrays.setArrays(internalArrays);
  }

}
