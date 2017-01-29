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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

import org.junit.Test;

public class Arrays_array_Test {

  @Test
  public void should_return_parameter() {
    Object[] array = { "one", "two" };
    assertThat(array).isSameAs(Arrays.array(array));
  }

  @Test
  public void should_return_an_int_array_from_AtomicIntegerArray() {
    // GIVEN
    int[] expected = new int[] { 1, 2, 3, 4 };
    AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(expected);
    // WHEN
    int[] actual = array(atomicIntegerArray);
    // THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_return_null_if_given_a_null_AtomicIntegerArray() {
    // GIVEN
    AtomicIntegerArray atomicIntegerArray = null;
    // WHEN
    int[] actual = array(atomicIntegerArray);
    // THEN
    assertThat(actual).isNull();
  }
  
  @Test
  public void should_return_an_long_array_from_AtomicLongArray() {
    // GIVEN
    long[] expected = new long[] { 1, 2, 3, 4 };
    AtomicLongArray atomicLongArray = new AtomicLongArray(expected);
    // WHEN
    long[] actual = array(atomicLongArray);
    // THEN
    assertThat(actual).isEqualTo(expected);
  }
  
  @Test
  public void should_return_null_if_given_a_null_AtomicLongArray() {
    // GIVEN
    AtomicLongArray atomicLongArray = null;
    // WHEN
    long[] actual = array(atomicLongArray);
    // THEN
    assertThat(actual).isNull();
  }
}
