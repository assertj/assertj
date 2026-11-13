package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.BDDSoftAssertions;

public class BDDSoftAssertionsExample {

  // tag::basic-bdd-soft-assertions[]
  @Test
  void basic_bdd_soft_assertions_example() {
    BDDSoftAssertions softly = new BDDSoftAssertions(); 

    softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.then(42).as("response to Everything").isGreaterThan(100);
    softly.then("Gandalf").isEqualTo("Sauron");

    // Don't forget to call assertAll() otherwise no assertion errors are reported!
    softly.assertAll(); 
  }
  // end::basic-bdd-soft-assertions[]

}
