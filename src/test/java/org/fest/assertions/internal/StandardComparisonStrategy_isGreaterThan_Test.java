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
import static org.mockito.Mockito.*;

import java.awt.Rectangle;

import org.junit.Test;

/**
 * Tests for {@link StandardComparisonStrategy#isGreaterThan(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy_isGreaterThan_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  public void verify_that_isGreaterThan_delegates_to_compare_method() {
    Employee boss = mock(Employee.class);
    Employee young = new Employee(10000, 25);
    standardComparisonStrategy.isGreaterThan(boss, young);
    verify(boss).compareTo(young);
  }

  @Test
  public void should_pass() {
    Employee boss = new Employee(10000, 35);
    Employee young = new Employee(10000, 25);
    assertTrue(standardComparisonStrategy.isGreaterThan(boss, young));
    assertFalse(standardComparisonStrategy.isGreaterThan(young, boss));
    assertFalse(standardComparisonStrategy.isGreaterThan(boss, boss));
  }

  @Test
  public void should_fail_if_first_parameter_is_not_comparable() {
    thrown.expect(IllegalArgumentException.class);
    standardComparisonStrategy.isGreaterThan(new Rectangle(), "foo");
  }
}
