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
package org.assertj.core.api.atomic.integerarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.IntArrays.arrayOf;
import static org.mockito.Mockito.verify;

import java.util.concurrent.atomic.AtomicIntegerArray;

import org.assertj.core.api.AtomicIntegerArrayAssert;
import org.assertj.core.api.AtomicIntegerArrayAssertBaseTest;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Test;

public class AtomicIntegerArrayAssert_hasArray_Test extends AtomicIntegerArrayAssertBaseTest {

  @Override
  protected AtomicIntegerArrayAssert invoke_api_method() {
    return assertions.hasArray(arrayOf(1, 2));
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactly(info(), internalArray(), arrayOf(1, 2));
  }
  
  @Test
  public void should_honor_the_given_element_comparator() {
    AtomicIntegerArray actual = new AtomicIntegerArray(new int[] { 1, 2, 3});
    assertThat(actual).usingElementComparator(new AbsValueComparator<Integer>()).hasArray(new int[] { -1, 2, 3});
  }

}
