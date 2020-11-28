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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for testing <code>{@link Long2DArrays}</code>.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Long2DArrays#failures} appropriately.
 *
 * @author Maciej Wajcht
 */
public class Long2DArraysBaseTest {

  /**
   * is initialized with {@link #initActualArray()} with default value = {{0, 2, 4}, {6, 8, 10}}
   */
  protected long[][] actual;
  protected Failures failures;
  protected Long2DArrays long2dArrays;
  protected Arrays2D arrays2d;
  protected AssertionInfo info = someInfo();

  @BeforeEach
  public void setUp() {
    failures = spy(new Failures());
    long2dArrays = new Long2DArrays();
    long2dArrays.failures = failures;
    arrays2d = mock(Arrays2D.class);
    long2dArrays.setArrays(arrays2d);
    initActualArray();
  }

  protected void initActualArray() {
    actual = new long[][] { { 0, 2, 4 }, { 6, 8, 10 } };
  }

}
