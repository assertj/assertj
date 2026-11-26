/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static example.data.Race.HOBBIT;
import static java.lang.String.format;

import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import example.core.GoTSoftAssertionsProvider.GoTCharacter;
import example.data.TolkienCharacter;

// not run by the build (class name does not match *Test)
class CustomSoftAssertionsExample {

  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
  private final GoTCharacter nedStark = new GoTCharacter("Ned Stark", true);

  @Test
  void tolkien_soft_assertions() {
    // tag::tolkien_soft_assertions[]
    TolkienSoftAssertions softly = new TolkienSoftAssertions();
    softly.assertThat(frodo).hasRace(HOBBIT)
                            .hasName("Frodo");
    // end::tolkien_soft_assertions[]
  }

  @Test
  void fantasy_soft_assertions() {
    // tag::fantasy_soft_assertions[]
    FantasySoftAssertions softly = new FantasySoftAssertions();

    // custom TolkienCharacter assertions
    softly.assertThat(frodo).hasRace(HOBBIT);

    // custom GoTCharacter assertions
    softly.assertThat(nedStark).isDead();

    // standard assertions
    softly.assertThat("Games of Thrones").startsWith("Games")
                                         .endsWith("Thrones");
    // verify assertions
    softly.assertAll();
    // end::fantasy_soft_assertions[]
  }

  @Test
  void after_assertion_error_collected() {
    // tag::after_assertion_error_collected[]
    SoftAssertions softly = new SoftAssertions();
    StringBuilder reportBuilder = new StringBuilder(format("Assertions report:%n"));

    // register our callback
    softly.setAfterAssertionErrorCollected(error -> reportBuilder.append(format("------------------%n%s%n", error.getMessage())));
    // the AssertionError corresponding to the failing assertions are registered in the report
    softly.assertThat("The Beatles").isEqualTo("The Rolling Stones");
    softly.assertThat(123).isEqualTo(123)
                          .isEqualTo(456);
    // end::after_assertion_error_collected[]
  }

  @Test
  void on_assertion_error_collected() {
    // tag::on_assertion_error_collected[]
    TolkienSoftAssertions softly = new TolkienSoftAssertions();

    TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

    // the AssertionError corresponding to this failing assertion is printed to the console.
    softly.assertThat(frodo).hasName("Bilbo");
    // end::on_assertion_error_collected[]
  }
}

// tag::tolkien_soft_assertions_class[]
class TolkienSoftAssertions extends AbstractSoftAssertions {

  public TolkienCharacterAssert assertThat(TolkienCharacter actual) {
    return proxy(TolkienCharacterAssert.class, TolkienCharacter.class, actual);
  }

  @Override
  public void onAssertionErrorCollected(AssertionError assertionError) {
    System.out.println(assertionError);
  }
}
// end::tolkien_soft_assertions_class[]
// @formatter:on
