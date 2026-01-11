/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Arrays;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for testing <code>{@link ObjectArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and
 * another with {@link ComparatorBasedComparisonStrategy}.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ObjectArraysBaseTest {

  protected static final AssertionInfo INFO = TestData.someInfo();

  /**
   * is initialized with {@link #initActualArray()}
   */
  protected String[] actual;
  protected Failures failures;
  protected ObjectArrays arrays;
  protected Conditions conditions;

  protected ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy;
  protected ObjectArrays arraysWithCustomComparisonStrategy;

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
    arrays = new ObjectArrays(StandardComparisonStrategy.instance());
    writeField(arrays, "failures", failures);

    caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    arraysWithCustomComparisonStrategy = new ObjectArrays(caseInsensitiveStringComparisonStrategy);
    writeField(arraysWithCustomComparisonStrategy, "failures", failures);
    conditions = spy(Conditions.instance());
    writeField(arrays, "conditions", conditions);
    initActualArray();
  }

  protected void initActualArray() {
    actual = array("Luke", "Yoda", "Leia");
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return CaseInsensitiveStringComparator.INSTANCE;
  }

  protected void setArrays(Arrays internalArrays) {
    writeField(arrays, "arrays", internalArrays);
  }

}
