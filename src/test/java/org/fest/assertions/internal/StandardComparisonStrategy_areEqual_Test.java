/*
 * Created on Sep 23, 2006
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2006-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.junit.Assert.*;

import org.fest.assertions.internal.StandardComparisonStrategy;
import org.fest.util.Objects;
import org.junit.Test;

/**
 * Tests for {@link StandardComparisonStrategy#areEqual(Object, Object)}.<br>
 * Conceptually the same as {@link Objects#areEqual(Object, Object)} but I don't know how to verify/test that
 * {@link StandardComparisonStrategy#areEqual(Object, Object)} simply calls {@link Objects#areEqual(Object, Object)}
 * 
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy_areEqual_Test {

  private static StandardComparisonStrategy standardComparisonStrategy = StandardComparisonStrategy.instance();

  @Test
  public void should_return_true_if_both_Objects_are_null_with_verify() {
    assertTrue(standardComparisonStrategy.areEqual(null, null));
  }

  @Test
  public void should_return_true_if_both_Objects_are_null() {
    assertTrue(standardComparisonStrategy.areEqual(null, null));
  }

  @Test
  public void should_return_true_if_Objects_are_equal() {
    assertTrue(standardComparisonStrategy.areEqual("Yoda", "Yoda"));
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
    assertFalse(standardComparisonStrategy.areEqual(null, "Yoda"));
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
    assertFalse(standardComparisonStrategy.areEqual("Yoda", null));
  }

  @Test
  public void should_return_are_not_equal_if_Objects_are_not_equal() {
    assertFalse(standardComparisonStrategy.areEqual("Yoda", 2));
  }

  @Test
  public void should_return_true_if_arrays_of_Objects_are_equal() {
    Object[] a1 = { "Luke", "Yoda", "Leia" };
    Object[] a2 = { "Luke", "Yoda", "Leia" };
    assertTrue(standardComparisonStrategy.areEqual(a1, a2));
  }

  @Test
  public void should_return_true_if_arrays_of_primitives_are_equal() {
    int[] a1 = { 6, 8, 10 };
    int[] a2 = { 6, 8, 10 };
    assertTrue(standardComparisonStrategy.areEqual(a1, a2));
  }

  @Test
  public void should_return_false_if_arrays_of_Objects_are_not_equal() {
    Object[] a1 = { "Luke", "Yoda", "Leia" };
    Object[] a2 = new Object[0];
    assertFalse(standardComparisonStrategy.areEqual(a1, a2));
  }

  @Test
  public void should_return_false_if_arrays_of_primitives_are_not_equal() {
    int[] a1 = { 6, 8, 10 };
    boolean[] a2 = { true };
    assertFalse(standardComparisonStrategy.areEqual(a1, a2));
  }

}
