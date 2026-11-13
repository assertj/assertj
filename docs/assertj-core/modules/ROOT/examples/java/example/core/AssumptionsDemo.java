package example.core;

// @formatter:off

import org.junit.jupiter.api.Test;

class AssumptionsDemo {

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
