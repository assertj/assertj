package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;

public class AutoCloseableSoftAssertionsExample {

  // tag::closeable-soft-assertions[]
  @Test
  void auto_closeable_soft_assertions_example() {
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
    softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");  // <2>
    softly.assertThat(42).as("response to Everything").isGreaterThan(100); // <2>
    softly.assertThat("Gandalf").isEqualTo("Sauron"); // <2>
      // no need to call assertAll, this is done when softly is closed.
    }
  }
  // end::closeable-soft-assertions[]

}
