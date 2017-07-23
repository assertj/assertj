/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.chararrays;

import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.Arrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.Test;

public class CharArrays_assertContainsAnyOf_Test extends CharArraysBaseTest {

  private Arrays internalArrays;

  @Override
  public void setUp() {
    super.setUp();
    internalArrays = mock(Arrays.class);
    setArrays(internalArrays);
  }

  @Test
  public void should_delegate_to_internal_Arrays() {
    arrays.assertContainsAnyOf(someInfo(), actual, new char[] { 'a', 'b', 'c' });
    verify(internalArrays).assertContainsAnyOf(someInfo(), failures, actual, new char[] { 'a', 'b', 'c' });
  }

}
