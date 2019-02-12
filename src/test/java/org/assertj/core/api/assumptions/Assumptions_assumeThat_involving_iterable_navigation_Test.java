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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;

import org.assertj.core.test.Jedi;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Assumptions_assumeThat_involving_iterable_navigation_Test {

  private Set<Jedi> jedis;
  private Jedi yoda;
  private Jedi luke;

  @BeforeEach
  public void setup() {
    yoda = new Jedi("Yoda", "green");
    luke = new Jedi("Luke", "green");
    jedis = newLinkedHashSet(yoda, luke);
  }

  @Test
  public void should_run_test_when_assumption_on_size_passes() {
    assertThatCode(() -> assumeThat(jedis).size().isLessThan(3)).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_back_to_iterable_passes() {
    assertThatCode(() -> assumeThat(jedis).size().isLessThan(3).returnToIterable()
                                          .hasSize(2)).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_back_to_list_passes() {
    assertThatCode(() -> assumeThat(newArrayList(jedis)).size().isLessThan(3)
                                                        .returnToIterable().hasSize(2)).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_after_navigating_to_elements_passes() {
    assertThatCode(() -> {
      assumeThat(jedis).first().isEqualTo(yoda);
      assumeThat(jedis).last().isEqualTo(luke);
      assumeThat(jedis).element(1).isEqualTo(luke);
    }).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_on_size_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(jedis).size()
                                                                                                   .as("check size")
                                                                                                   .isGreaterThan(3));
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_first_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(jedis).first()
                                                                                                   .as("check first element")
                                                                                                   .isEqualTo(luke));
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_last_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(jedis).last()
                                                                                                   .as("check last element")
                                                                                                   .isEqualTo(yoda));
  }

  @Test
  public void should_ignore_test_when_assumption_after_navigating_to_element_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(jedis).element(1)
                                                                                                   .as("check element at index 1")
                                                                                                   .isEqualTo(yoda));
  }

}
