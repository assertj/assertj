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
package org.assertj.core.internal.double2darrays;

import static org.assertj.core.data.Index.atIndex;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Double2DArrays;
import org.assertj.core.internal.Double2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Double2DArrays#assertDoesNotContain(AssertionInfo, double[][], double[], Index)}</code>.
 *
 * @author Maciej Wajcht
 */
public class Double2DArrays_assertDoesNotContain_at_Index_Test extends Double2DArraysBaseTest {

  @Test
  public void should_delegate_to_Arrays2D() {
    // GIVEN
    double[] doubles = new double[] { 0.0, 2.0, 4.0 };
    // WHEN
    double2dArrays.assertDoesNotContain(info, actual, doubles, atIndex(1));
    // THEN
    verify(arrays2d).assertDoesNotContain(info, failures, actual, doubles, atIndex(1));
  }
}
