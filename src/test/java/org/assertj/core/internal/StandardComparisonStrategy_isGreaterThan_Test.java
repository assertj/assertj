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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Rectangle;

import org.assertj.core.util.Employee;
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
    assertThat(standardComparisonStrategy.isGreaterThan(boss, young)).isTrue();
    assertThat(standardComparisonStrategy.isGreaterThan(young, boss)).isFalse();
    assertThat(standardComparisonStrategy.isGreaterThan(boss, boss)).isFalse();
  }

  @Test
  public void should_fail_if_first_parameter_is_not_comparable() {
    thrown.expectIllegalArgumentException();
    standardComparisonStrategy.isGreaterThan(new Rectangle(), "foo");
  }
}
