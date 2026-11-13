package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;

public class JUnit4SoftAssertionsExample {

  // tag::junit4-soft-assertions[]
  @Rule
  public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

  @Test
  void junit4_soft_assertions_example() {
    softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");  // <2>
    softly.assertThat(42).as("response to Everything").isGreaterThan(100); // <2>
    softly.assertThat("Gandalf").isEqualTo("Sauron"); // <2>
    // No need to call softly.assertAll(), this is automatically done by the JUnitSoftAssertions rule
  }
  // end::junit4-soft-assertions[]

}
