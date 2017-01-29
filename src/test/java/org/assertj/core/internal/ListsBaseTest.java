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

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Lists}</code>, set up an instance with {@link StandardComparisonStrategy} and another with
 * {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Lists#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class ListsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Lists listsWithCaseInsensitiveComparisonStrategy;

  protected Failures failures;
  protected Lists lists;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    lists = new Lists();
    lists.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    listsWithCaseInsensitiveComparisonStrategy = new Lists(comparisonStrategy);
    listsWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}