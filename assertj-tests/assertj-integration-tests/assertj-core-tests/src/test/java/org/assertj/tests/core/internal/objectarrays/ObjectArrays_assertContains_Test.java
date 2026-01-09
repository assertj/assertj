/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertContains_Test extends ObjectArraysBaseTest {

  private Arrays internalArrays;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    internalArrays = mock(Arrays.class);
    setArrays(internalArrays);
  }

  @Test
  void should_delegate_to_internal_Arrays() {
    // WHEN
    arrays.assertContains(INFO, actual, array("Luke", "Yoda", "Leia"));
    // THEN
    verify(internalArrays).assertContains(INFO, failures, actual, array("Luke", "Yoda", "Leia"));
  }

}
