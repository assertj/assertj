/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.short2darrays;

import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Arrays2D;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Short2DArrays;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Maciej Wajcht
 */
class Short2DArraysBaseTest {

  protected short[][] actual;
  protected Failures failures;
  protected Short2DArrays short2DArrays;
  protected Arrays2D arrays2d;
  protected AssertionInfo info = TestData.someInfo();

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
    short2DArrays = new Short2DArrays();
    writeField(short2DArrays, "failures", failures);

    arrays2d = mock(Arrays2D.class);
    short2DArrays.setArrays(arrays2d);
    initActualArray();
  }

  protected void initActualArray() {
    actual = new short[][] { { 0, 2, 4 }, { 6, 8, 10 } };
  }

}
