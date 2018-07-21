/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.awt.Rectangle;

import org.assertj.core.util.Employee;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StandardComparisonStrategy#isGreaterThanOrEqualTo(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy_isGreaterThanOrEqualTo_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  public void should_pass() {
    Employee boss = new Employee(10000, 35);
    Employee young = new Employee(10000, 25);
    assertThat(standardComparisonStrategy.isGreaterThanOrEqualTo(boss, young)).isTrue();
    assertThat(standardComparisonStrategy.isGreaterThanOrEqualTo(boss, boss)).isTrue();
    assertThat(standardComparisonStrategy.isGreaterThanOrEqualTo(young, boss)).isFalse();
  }

  @Test
  public void should_fail_if_a_parameter_is_not_comparable() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      Rectangle r1 = new Rectangle(10, 20);
      Rectangle r2 = new Rectangle(20, 10);
      standardComparisonStrategy.isGreaterThanOrEqualTo(r1, r2);
    });
  }

}
