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
package org.assertj.core.internal.byte2darrays;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Byte2DArrays;
import org.assertj.core.internal.Byte2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Byte2DArrays#assertNullOrEmpty(AssertionInfo, byte[][])}</code>.
 *
 * @author Maciej Wajcht
 */
public class Byte2DArrays_assertNullOrEmpty_Test extends Byte2DArraysBaseTest {

  @Test
  public void should_delegate_to_Arrays2D() {
    // WHEN
    byte2dArrays.assertNullOrEmpty(info, actual);
    // THEN
    verify(arrays2d).assertNullOrEmpty(info, failures, actual);
  }
}
