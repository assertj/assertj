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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.spy;

/**
 * 
 * Base class for {@link Spliterators} tests.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Spliterators#setFailures(Failures)} appropriately.
 * 
 * @author William Bakker
 */
public class SpliteratorsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Failures failures;
  protected Spliterators spliterators;

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    spliterators = new Spliterators();
    spliterators.setFailures(failures);
  }

  @AfterEach
  public void tearDown() {
    spliterators.setFailures(Failures.instance());
  }

}
