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

import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#areEqual(Object, Object)}.<br>
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_areEqual_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_return_true_if_objects_are_equal_according_to_comparison_strategy() {
    assertTrue(caseInsensitiveComparisonStrategy.areEqual("Yoda", "Yoda"));
    assertTrue(caseInsensitiveComparisonStrategy.areEqual("Yoda", "YODA"));
    assertTrue(caseInsensitiveComparisonStrategy.areEqual("YOda", "YodA"));
  }

  @Test
  public void should_return_true_if_both_objects_are_null() {
    assertTrue(caseInsensitiveComparisonStrategy.areEqual(null, null));
  }

  @Test
  public void should_return_false_if_first_object_is_null_and_second_is_not() {
    assertFalse(caseInsensitiveComparisonStrategy.areEqual(null, "Yoda"));
  }

  @Test
  public void should_return_false_if_second_object_is_null_and_first_is_not() {
    assertFalse(caseInsensitiveComparisonStrategy.areEqual("Yoda", null));
  }

  @Test
  public void should_return_false_if_objects_are_not_equal_according_to_comparison_strategy() {
    assertFalse(caseInsensitiveComparisonStrategy.areEqual("Yoda", "Yod"));
  }

  @Test
  public void should_fail_if_objects_are_not_mutually_comparable() {
    thrown.expect(ClassCastException.class);
    assertFalse(caseInsensitiveComparisonStrategy.areEqual("Yoda", 5));
  }

}
