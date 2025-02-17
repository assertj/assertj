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
package org.assertj.core.internal.chararrays;

import static org.assertj.core.testkit.CharArrays.arrayOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Arrays;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharArrays#assertContains(AssertionInfo, char[], char[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class CharArrays_assertContains_Test extends CharArraysBaseTest {

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
    arrays.assertContains(someInfo(), actual, arrayOf('a', 'b', 'c'));
    verify(internalArrays).assertContains(someInfo(), failures, actual, arrayOf('a', 'b', 'c'));
  }

}
