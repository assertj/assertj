package example.core;

// tag::user_guide[]
import static org.assertj.core.api.Assertions.assertThat; // <1>

import org.junit.jupiter.api.Test;

public class SimpleAssertionsExample {

  @Test
  void a_few_simple_assertions() {
    assertThat("The Lord of the Rings").isNotNull()  // <2> <3>
                                       .startsWith("The") // <4>
                                       .contains("Lord") // <4>
                                       .endsWith("Rings"); // <4>
  }

}
// end::user_guide[]
