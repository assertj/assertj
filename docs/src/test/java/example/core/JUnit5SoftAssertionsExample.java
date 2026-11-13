package example.core;

// tag::junit5-soft-assertions[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;

@ExtendWith(SoftAssertionsExtension.class)
public class JUnit5SoftAssertionsExample {

  @Test
  void junit5_soft_assertions_multiple_failures_example(SoftAssertions softly) {
    softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.assertThat(42).as("response to Everything").isGreaterThan(100);
    softly.assertThat("Gandalf").isEqualTo("Sauron");
    // No need to call softly.assertAll(), this is automatically done by the SoftAssertionsExtension
  }

  @Test
  void junit5_bdd_soft_assertions_multiple_failures_example(BDDSoftAssertions softly) {
    softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.then(42).as("response to Everything").isGreaterThan(100);
    softly.then("Gandalf").isEqualTo("Sauron");
    // No need to call softly.assertAll(), this is automatically done by the SoftAssertionsExtension
  }

  @ParameterizedTest
  @CsvSource({ "1, 1, 2", "1, 2, 3" })
  // test parameters come first, soft assertion must come last.
  void junit5_soft_assertions_parameterized_test_example(int a, int b, int sum, SoftAssertions softly) {
    softly.assertThat(a + b).as("sum").isEqualTo(sum);
    softly.assertThat(a).isLessThan(sum);
    softly.assertThat(b).isLessThan(sum);
  }

}
// end::junit5-soft-assertions[]
