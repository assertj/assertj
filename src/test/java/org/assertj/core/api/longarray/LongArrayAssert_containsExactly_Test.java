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
package org.assertj.core.api.longarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.LongArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.LongArrayAssert;
import org.assertj.core.api.LongArrayAssertBaseTest;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.LongArrayAssert#containsExactly(long...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class LongArrayAssert_containsExactly_Test extends LongArrayAssertBaseTest {

  @Override
  protected LongArrayAssert invoke_api_method() {
    return assertions.containsExactly(1L, 2L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactly(getInfo(assertions), getActual(assertions), arrayOf(1L, 2L));
  }

  @Test
  public void should_honor_the_given_element_comparator() {
    long[] actual = new long[] { 1, 2, 3, 4 };
    assertThat(actual).usingElementComparator(new AbsValueComparator<Long>()).containsExactly(-1, 2, 3, -4);
  }
  
}
