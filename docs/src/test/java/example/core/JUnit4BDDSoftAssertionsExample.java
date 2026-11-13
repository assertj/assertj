package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;

public class JUnit4BDDSoftAssertionsExample {

  // tag::junit4-bdd-soft-assertions[]
  @Rule
  public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

  @Test
  void junit4_bdd_soft_assertions_example() {
    softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.then(42).as("response to Everything").isGreaterThan(100);
    softly.then("Gandalf").isEqualTo("Dauron");
    // No need to call softly.assertAll(), this is automatically done by the JUnitSoftAssertions rule
  }
  // end::junit4-bdd-soft-assertions[]

}
