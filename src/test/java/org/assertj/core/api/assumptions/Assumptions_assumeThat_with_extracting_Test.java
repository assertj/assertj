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
package org.assertj.core.api.assumptions;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Set;

import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Jedi;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class Assumptions_assumeThat_with_extracting_Test {

  private static int ranTests = 0;
  private Set<Jedi> jedis;
  private Jedi yoda;
  private Jedi luke;

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  @Before
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

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(3);
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_list_passes() {
    assumeThat(jedis).extracting("name").contains("Luke");
    ranTests++;
  }

  @Test
  public void should_run_test_when_assumption_using_extracting_on_object_passes() {
    assumeThat(yoda).extracting("name").containsExactly("Yoda");
    ranTests++;
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_flatExtracting_children() {
    assumeThat(newArrayList(homer, fred)).flatExtracting("children")
                                         .containsOnly(bart, lisa, maggie, pebbles);
    ranTests++;
  }

  @Test
  public void should_ignore_test_when_assumption_using_extracting_fails() {
    assumeThat(jedis).extracting("name").contains("Vader");
    fail("should not arrive here");
  }
}
