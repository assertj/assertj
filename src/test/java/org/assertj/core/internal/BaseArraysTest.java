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

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;

public class BaseArraysTest {

  @Rule
  public ExpectedException thrown = none();

  protected Arrays arrays;
  protected Failures failures;
  protected String[] actual;
  protected ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy;
  protected Arrays arraysWithCustomComparisonStrategy;

  @Before
  public void setUp() {
    actual = array("Luke", "Yoda", "Leia");
    failures = spy(new Failures());
    arrays = Arrays.instance();
    caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(new CaseInsensitiveStringComparator());
    arraysWithCustomComparisonStrategy = new Arrays(caseInsensitiveStringComparisonStrategy);
  }

}