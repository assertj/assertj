package org.assertj.core.api.abstract_;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.TolkienCharacter;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.TolkienCharacter.Race.DRAGON;

class AbstractAssert_getActual_Test {

  @Test
  void should_pass_if_optional_contains_the_expected_object_reference() {
    Optional<String> tested = Optional.of("something");

    String innerValue = assertThat(tested).get().getActual();

    assertThat(innerValue).isEqualTo("something");
  }

  @Test
  void should_pass_if_optional_contains_the_expected_object_reference2() {
    TolkienCharacter tested = TolkienCharacter.of("smaug", 1, DRAGON);

    String innerValue = assertThat(tested).extracting("name", as(InstanceOfAssertFactories.STRING)).getActual();

    assertThat(innerValue).isEqualTo("smaug");
  }
}
