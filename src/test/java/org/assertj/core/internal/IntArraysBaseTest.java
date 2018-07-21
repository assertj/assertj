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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.test.IntArrays.arrayOf;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;


/**
 * Base class for testing <code>{@link IntArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and another
 * with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link IntArrays#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class IntArraysBaseTest {

  /**
   * is initialized with {@link #initActualArray()} with default value = {6, 8, 10}
   */
  protected int[] actual;
  protected Failures failures;
  protected IntArrays arrays;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected IntArrays arraysWithCustomComparisonStrategy;

  private AbsValueComparator<Integer> absValueComparator = new AbsValueComparator<>();

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    arrays = new IntArrays();
    arrays.failures = failures;
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    arraysWithCustomComparisonStrategy = new IntArrays(absValueComparisonStrategy);
    arraysWithCustomComparisonStrategy.failures = failures;
    initActualArray();
  }

  protected void initActualArray() {
    actual = arrayOf(6, 8, 10);
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return absValueComparator;
  }

  protected void setArrays(Arrays internalArrays) {
    arrays.setArrays(internalArrays);
  }

}