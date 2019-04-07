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

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Map;
import java.util.Set;

import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Jedi;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Assumptions_assumeThat_with_extracting_Test {

  private Set<Jedi> jedis;
  private Jedi yoda;
  private Jedi luke;

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  @BeforeEach
  public void setup() {
    yoda = new Jedi("Yoda", "green");
    luke = new Jedi("Luke", "green");
    jedis = newHashSet(yoda, luke);

    bart = new CartoonCharacter("Bart Simpson");
    lisa = new CartoonCharacter("Lisa Simpson");
    maggie = new CartoonCharacter("Maggie Simpson");
    homer = new CartoonCharacter("Homer Simpson");
    homer.addChildren(bart, lisa, maggie);

    pebbles = new CartoonCharacter("Pebbles Flintstone");
    fred = new CartoonCharacter("Fred Flintstone");
    fred.addChildren(pebbles);
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_list_passes() {
    assertThatCode(() -> assumeThat(jedis).extracting("name").contains("Luke")).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_object_with_single_parameter_passes() {
    assertThatCode(() -> assumeThat(yoda).extracting("name").isEqualTo("Yoda")).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_map_with_single_parameter_passes() {
    Map<Jedi, String> jedis = mapOf(entry(yoda, "master"), entry(luke, "padawan"));
    assertThatCode(() -> assumeThat(jedis).extracting(yoda).isEqualTo("master")).doesNotThrowAnyException();
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_object_with_multiple_parameters_passes() {
    assertThatCode(() -> assumeThat(yoda).extracting("name", "class")
                                         .containsExactly("Yoda", Jedi.class)).doesNotThrowAnyException();
  }

  @Test
  public void should_allow_assumptions_with_flatExtracting() {
    assertThatCode(() -> assumeThat(newArrayList(homer, fred)).flatExtracting("children")
                                                              .containsOnly(bart, lisa, maggie, pebbles))
                                                                                                         .doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_using_extracting_fails() {
    assertThatExceptionOfType(AssumptionViolatedException.class).isThrownBy(() -> assumeThat(jedis).extracting("name")
                                                                                                   .contains("Vader"));
  }
}
