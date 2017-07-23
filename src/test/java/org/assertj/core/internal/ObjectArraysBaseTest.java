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

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link ObjectArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and
 * another with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link ObjectArrays#failures} appropriately.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ObjectArraysBaseTest {

  @Rule
  public ExpectedException thrown = none();

  /**
   * is initialized with {@link #initActualArray()}
   */
  protected String[] actual;
  protected Failures failures;
  protected ObjectArrays arrays;
  protected Conditions conditions;

  protected ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy;
  protected ObjectArrays arraysWithCustomComparisonStrategy;

  private CaseInsensitiveStringComparator caseInsensitiveStringComparator = new CaseInsensitiveStringComparator();

  @Before
  public void setUp() {
    failures = spy(new Failures());
    arrays = new ObjectArrays();
    arrays.failures = failures;
    caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    arraysWithCustomComparisonStrategy = new ObjectArrays(caseInsensitiveStringComparisonStrategy);
    arraysWithCustomComparisonStrategy.failures = failures;
    conditions = spy(new Conditions());
    arrays.conditions = conditions;
    initActualArray();
  }

  protected void initActualArray() {
    actual = array("Luke", "Yoda", "Leia");
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return caseInsensitiveStringComparator;
  }

  protected void setArrays(Arrays internalArrays) {
    arrays.setArrays(internalArrays);
  }

}