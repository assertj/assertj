package example.core;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.SoftAssertions;

public class SoftAssertionsExample {

  // tag::basic-soft-assertions[]
  @Test
  void basic_soft_assertions_example() {
    SoftAssertions softly = new SoftAssertions(); // <1>

    softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");  // <2>
    softly.assertThat(42).as("response to Everything").isGreaterThan(100); // <2>
    softly.assertThat("Gandalf").isEqualTo("Sauron"); // <2>

    // Don't forget to call assertAll() otherwise no assertion errors are reported!
    softly.assertAll(); // <3>
  }
  // end::basic-soft-assertions[]

  // tag::assertSoftly-soft-assertions[]
  @Test
  void assertSoftly_example() {
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");
      softly.assertThat(42).as("response to Everything").isGreaterThan(100);
      softly.assertThat("Gandalf").isEqualTo("Sauron");
      // no need to call assertAll(), assertSoftly does it for us.
    });
  }
  // end::assertSoftly-soft-assertions[]

}
