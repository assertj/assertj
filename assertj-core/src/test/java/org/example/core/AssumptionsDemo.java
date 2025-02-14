package org.example.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.testkit.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.testkit.TolkienCharacter.Race.MAIA;

import java.util.Collections;
import java.util.Set;

import org.assertj.core.testkit.TolkienCharacter;
import org.junit.jupiter.api.Test;

class AssumptionsDemo {

  TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
  TolkienCharacter sauron = TolkienCharacter.of("Sauron", 9999, MAIA);
  Set<TolkienCharacter> fellowshipOfTheRing = Collections.singleton(sauron);

  // tag::assumption_not_met[]
  @Test
  public void when_an_assumption_is_not_met_the_test_is_ignored() {
    // since this assumption is obviously false ...
    assumeThat(frodo.getRace()).isEqualTo(HOBBIT);
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
