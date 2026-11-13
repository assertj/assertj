package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;

public class AutoCloseableBDDSoftAssertionsExample {

  // tag::closeable-bdd-soft-assertions[]
  @Test
  void auto_closeable_bdd_soft_assertions_example() {
    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
      softly.then(42).as("response to Everything").isGreaterThan(100);
      softly.then("Gandalf").isEqualTo("Sauron");
      // no need to call assertAll, this is done when softly is closed.
    }
  }
  // end::closeable-bdd-soft-assertions[]

}
