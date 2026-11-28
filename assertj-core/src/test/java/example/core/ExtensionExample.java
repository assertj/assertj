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

import static example.core.TolkienCharacterAssert.assertThat;
import static example.data.Race.HOBBIT;
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.util.Lists.list;
// tag::condition_import[]
import static org.assertj.core.util.Sets.newLinkedHashSet;
// end::condition_import[]

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

// not run by the build (class name does not match *Test), one example fails on purpose
class ExtensionExample {

  // tag::condition_creation[]
  static Set<String> JEDIS = newLinkedHashSet("Luke", "Yoda", "Obiwan");

  // implementation with Java 8 lambda
  Condition<String> jediPower = new Condition<>(JEDIS::contains, "jedi power");

  // implementation with Java 7
  Condition<String> jedi = new Condition<String>("jedi") {
    @Override
    public boolean matches(String value) {
      return JEDIS.contains(value);
    }
  };
  // end::condition_creation[]

  // tag::sith_condition[]
  List<String> SITHS = list("Sidious", "Vader", "Plagueis");
  Condition<String> sith = new Condition<>(SITHS::contains, "sith");
  // end::sith_condition[]

  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

  @Test
  void condition() {
    // tag::condition[]
    // jedi is a Condition
    assertThat("Yoda").is(jedi);
    // end::condition[]
  }

  @Test
  void condition_usage() {
    // tag::condition_usage[]
    assertThat("Yoda").is(jedi);
    assertThat("Vader").isNot(jedi);

    assertThat("Yoda").has(jediPower);
    assertThat("Solo").doesNotHave(jediPower);
    // end::condition_usage[]
  }

  @Test
  void condition_error_message() {
    // tag::condition_error_message[]
    assertThat("Vader").is(jedi);
    // end::condition_error_message[]
  }

  @Test
  void condition_usage_with_collections() {
    // tag::condition_usage_with_collections[]
    assertThat(list("Luke", "Yoda")).are(jedi);
    assertThat(list("Leia", "Solo")).areNot(jedi);

    assertThat(list("Luke", "Yoda")).have(jediPower);
    assertThat(list("Leia", "Solo")).doNotHave(jediPower);

    assertThat(list("Luke", "Yoda", "Leia")).areAtLeast(2, jedi);
    assertThat(list("Luke", "Yoda", "Leia")).haveAtLeast(2, jediPower);

    assertThat(list("Luke", "Yoda", "Leia")).areAtMost(2, jedi);
    assertThat(list("Luke", "Yoda", "Leia")).haveAtMost(2, jediPower);

    assertThat(list("Luke", "Yoda", "Leia")).areExactly(2, jedi);
    assertThat(list("Luke", "Yoda", "Leia")).haveExactly(2, jediPower);
    // end::condition_usage_with_collections[]
  }

  @Test
  void combining_conditions() {
    // tag::combining_conditions[]
    assertThat("Vader").is(anyOf(jedi, sith));
    assertThat("Solo").is(allOf(not(jedi), not(sith)));
    // end::combining_conditions[]
  }

  @Test
  void custom_assertion_usage() {
    // tag::custom_assertion_usage[]
    // use assertThat from TolkienCharacterAssert to check TolkienCharacter
    TolkienCharacterAssert.assertThat(frodo).hasName("Frodo");

    // code is more elegant when TolkienCharacterAssert.assertThat is imported statically :
    assertThat(frodo).hasName("Frodo");
    // end::custom_assertion_usage[]
  }
}
// @formatter:on
