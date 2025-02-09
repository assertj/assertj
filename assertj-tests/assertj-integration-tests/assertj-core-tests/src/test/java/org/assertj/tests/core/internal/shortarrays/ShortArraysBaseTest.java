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
package org.assertj.tests.core.internal.shortarrays;

import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.assertj.tests.core.testkit.ShortArrays.arrayOf;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.ShortArrays;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for testing <code>{@link ShortArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and another
 * with {@link ComparatorBasedComparisonStrategy}. *
 *
 * @author Joel Costigliola
 */
class ShortArraysBaseTest {

  /**
   * is initialized with {@link #initActualArray()} with default value = {6, 8, 10}
   */
  protected short[] actual;
  protected Failures failures;
  protected ShortArrays arrays;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected ShortArrays arraysWithCustomComparisonStrategy;

  private final AbsValueComparator<Short> absValueComparator = new AbsValueComparator<>();

  @BeforeEach
  public void setUp() {
    initActualArray();
    failures = spy(Failures.instance());

    arrays = new ShortArrays(StandardComparisonStrategy.instance());
    writeField(arrays, "failures", failures);

    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    arraysWithCustomComparisonStrategy = new ShortArrays(absValueComparisonStrategy);
    writeField(arraysWithCustomComparisonStrategy, "failures", failures);
  }

  protected void initActualArray() {
    actual = arrayOf(6, 8, 10);
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return absValueComparator;
  }

}
