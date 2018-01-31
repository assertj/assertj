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
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;

import org.assertj.core.test.Jedi;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class Assumptions_assumeThat_involving_iterable_navigation_Test {

  private static int ranTests = 0;
  private Set<Jedi> jedis;
  private Jedi yoda;
  private Jedi luke;

  @Before
  public void setup() {
    yoda = new Jedi("Yoda", "green");
    luke = new Jedi("Luke", "green");
    jedis = newLinkedHashSet(yoda, luke);
  }

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(4);
  }

  @Test
  public void should_run_test_when_assumption_on_size_passes() {
    assumeThat(jedis).size().isLessThan(3);
    ranTests++;
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_back_to_iterable_passes() {
    assumeThat(jedis).size().isLessThan(3)
                     .returnToIterable().hasSize(2);
    ranTests++;
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_back_to_list_passes() {
    assumeThat(newArrayList(jedis)).size().isLessThan(3)
                                   .returnToIterable().hasSize(2);
    ranTests++;
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_to_elements_passes() {
    assumeThat(jedis).first().isEqualTo(yoda);
    assumeThat(jedis).last().isEqualTo(luke);
    assumeThat(jedis).element(1).isEqualTo(luke);
    ranTests++;
  }

  @Test
  public void should_ignore_test_when_assumption_on_size_fails() {
    assumeThat(jedis).size().as("check size").isGreaterThan(3);
    fail("should not arrive here");
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_first_fails() {
    assumeThat(jedis).first().as("check first element").isEqualTo(luke);
    fail("should not arrive here");
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_last_fails() {
    assumeThat(jedis).last().as("check last element").isEqualTo(yoda);
    fail("should not arrive here");
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_element_fails() {
    assumeThat(jedis).element(1).as("check element at index 1").isEqualTo(yoda);
    fail("should not arrive here");
  }

}
