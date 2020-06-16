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
package org.assertj.core.internal.boolean2darrays;

import static org.assertj.core.data.Index.atIndex;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Boolean2DArrays;
import org.assertj.core.internal.Boolean2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
* Tests for <code>{@link Boolean2DArrays#assertContains(AssertionInfo, boolean[][], boolean[], Index)}</code>.
 * 
 * @author Maciej Wajcht
 */
public class Boolean2DArrays_assertContains_at_Index_Test extends Boolean2DArraysBaseTest {

  @Test
  public void should_delegate_to_Arrays2D() {
    // GIVEN
    boolean[] booleans = new boolean[] { true, false, true };
    // WHEN
    boolean2dArrays.assertContains(info, actual, booleans, atIndex(1));
    // THEN
    verify(arrays2d).assertContains(info, failures, actual, booleans, atIndex(1));
  }
}
