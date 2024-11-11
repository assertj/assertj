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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.object2darrays;

import static org.mockito.Mockito.verify;

import org.assertj.core.internal.Object2DArraysBaseTest;
import org.junit.jupiter.api.Test;

class Object2DArrays_assertNumberOfRows_Test extends Object2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    object2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
