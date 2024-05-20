/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.assumptions;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;

import java.util.Set;

import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Assumptions_assumeThat_with_filteredOn_Test {

  private Set<Jedi> jedis;
  private Jedi yoda;
  private Jedi luke;

  @BeforeEach
  void setup() {
    yoda = new Jedi("Yoda", "green");
    luke = new Jedi("Luke", "green");
    jedis = newHashSet(yoda, luke);
  }

  @Test
  void should_run_test_when_assumption_with_filtered_elements_passes() {
    assertThatCode(() -> assumeThat(jedis).filteredOn("name", "Luke").contains(luke)).doesNotThrowAnyException();
  }

  @Test
  void should_ignore_test_when_assumption_with_filtered_elements_fails() {
    expectAssumptionNotMetException(() -> assumeThat(jedis).filteredOn("name", "Luke")
                                                           .contains(yoda));
  }
}
