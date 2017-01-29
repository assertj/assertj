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

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

/**
 * Tests for {@link Objects#areEqual(Object, Object)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Objects_areEqual_Test {

  @Test
  public void should_return_true_if_both_Objects_are_null() {
    assertThat(Objects.areEqual(null, null)).isTrue();
  }

  @Test
  public void should_return_true_if_Objects_are_equal() {
    assertThat(Objects.areEqual("Yoda", "Yoda")).isTrue();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
    assertThat(Objects.areEqual(null, "Yoda")).isFalse();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
    assertThat(Objects.areEqual("Yoda", null)).isFalse();
  }

  @Test
  public void should_return_are_not_equal_if_Objects_are_not_equal() {
    assertThat(Objects.areEqual("Yoda", 2)).isFalse();
  }

  @Test
  public void should_return_true_if_arrays_of_Objects_are_equal() {
    Object[] a1 = { "Luke", "Yoda", "Leia" };
    Object[] a2 = { "Luke", "Yoda", "Leia" };
    assertThat(Objects.areEqual(a1, a1)).isTrue();
    assertThat(Objects.areEqual(a1, a2)).isTrue();
  }

  @Test
  public void should_return_true_if_arrays_of_primitives_are_equal() {
    int[] a1 = { 6, 8, 10 };
    int[] a2 = { 6, 8, 10 };
    assertThat(Objects.areEqual(a1, a2)).isTrue();
  }

  @Test
  public void should_return_false_if_arrays_of_Objects_are_not_equal() {
    Object[] a1 = { "Luke", "Yoda", "Leia" };
    Object[] a2 = new Object[0];
    assertThat(Objects.areEqual(a1, a2)).isFalse();
  }

  @Test
  public void should_return_false_if_arrays_of_primitives_are_not_equal() {
    int[] a1 = { 6, 8, 10 };
    boolean[] a2 = { true };
    assertThat(Objects.areEqual(a1, a2)).isFalse();
  }

  @Test
  public void should_return_false_if_one_parameter_is_not_an_array() {
    Object[] a1 = { "Luke", "Yoda", "Leia" };
    assertThat(Objects.areEqual(a1, "")).isFalse();
    assertThat(Objects.areEqual("", a1)).isFalse();
  }
}
