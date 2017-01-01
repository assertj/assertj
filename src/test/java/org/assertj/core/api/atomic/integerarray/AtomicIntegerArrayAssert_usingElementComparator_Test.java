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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.atomic.integerarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.assertj.core.api.AtomicIntegerArrayAssert;
import org.assertj.core.api.AtomicIntegerArrayAssertBaseTest;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AtomicIntegerArrayAssert_usingElementComparator_Test extends AtomicIntegerArrayAssertBaseTest {

  @Mock
  private Comparator<Integer> comparator;

  private Objects objectsBefore;

  @Before
  public void before() {
    initMocks(this);
    objectsBefore = getObjects(assertions);
  }

  @Override
  protected AtomicIntegerArrayAssert invoke_api_method() {
    // in that test, the comparator type is not important, we only check that we correctly switch of comparator
    return assertions.usingElementComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(objectsBefore).isSameAs(getObjects(assertions));
    assertThat(comparator).isSameAs(getArrays(assertions).getComparator());
  }

  @Test
  public void should_honor_the_given_element_comparator() {
    AtomicIntegerArray actual = new AtomicIntegerArray(new int[] { 1, 2, 3, 4 });
    assertThat(actual).usingElementComparator(new AbsValueComparator<Integer>()).containsExactly(-1, 2, 3, -4);
  }

}
