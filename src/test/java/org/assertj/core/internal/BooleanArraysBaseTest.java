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

import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.spy;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link BooleanArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and
 * another with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link BooleanArrays#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class BooleanArraysBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected boolean[] actual;
  protected Failures failures;
  protected BooleanArrays arrays;

  @Before
  public void setUp() {
    actual = arrayOf(true, false);
    failures = spy(new Failures());
    arrays = new BooleanArrays();
    arrays.failures = failures;
  }

  protected void setArrays(Arrays internalArrays) {
    arrays.setArrays(internalArrays);
  }

}