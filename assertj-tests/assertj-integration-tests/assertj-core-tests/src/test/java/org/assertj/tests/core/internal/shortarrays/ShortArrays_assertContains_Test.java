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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.shortarrays;

import static org.assertj.tests.core.testkit.ShortArrays.arrayOf;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.testkit.FieldTestUtils.writeField;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Arrays;
import org.assertj.core.internal.ShortArrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrays#assertContains(AssertionInfo, short[], short[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ShortArrays_assertContains_Test extends ShortArraysBaseTest {

  private Arrays internalArrays;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    internalArrays = mock(Arrays.class);
    writeField(arrays, "arrays", internalArrays);
  }

  @Test
  void should_delegate_to_internal_Arrays() {
    arrays.assertContains(someInfo(), actual, arrayOf(6, 8, 10));
    verify(internalArrays).assertContains(someInfo(), failures, actual, arrayOf(6, 8, 10));
  }

}
