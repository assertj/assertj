/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static example.data.Race.HOBBIT;
import static example.data.Race.MAIA;
import static example.data.Race.ORC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

class AssumptionsDemo {

  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
  private final TolkienCharacter sauron = new TolkienCharacter("Sauron", 9000, MAIA);
  private final List<TolkienCharacter> fellowshipOfTheRing = Arrays.asList(frodo);

  // tag::assumption_not_met[]
  @Test
  public void when_an_assumption_is_not_met_the_test_is_ignored() {
    // since this assumption is obviously false ...
    assumeThat(frodo.getRace()).isEqualTo(ORC);
    // ... this assertion is not performed
    assertThat(fellowshipOfTheRing).contains(sauron);
  }
  // end::assumption_not_met[]

  // tag::assumption_met[]
  @Test
  public void when_all_assumptions_are_met_the_test_is_run_normally() {
    // since this assumption is true ...
    assumeThat(frodo.getRace()).isEqualTo(HOBBIT);
    // ... this assertion is performed
    assertThat(fellowshipOfTheRing).doesNotContain(sauron);
  }
  // end::assumption_met[]
}
// @formatter:on
