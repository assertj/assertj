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
package org.assertj.core.internal.short2darrays;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Short2DArrays;
import org.assertj.core.internal.Short2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Short2DArrays#assertNullOrEmpty(AssertionInfo, short[][])}</code>.
 *
 * @author Maciej Wajcht
 */
public class Short2DArrays_assertNullOrEmpty_Test extends Short2DArraysBaseTest {

  @Test
  public void should_delegate_to_Arrays2D() {
    // WHEN
    short2DArrays.assertNullOrEmpty(info, actual);
    // THEN
    verify(arrays2d).assertNullOrEmpty(info, failures, actual);
  }
}
