/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.assumptionRunner;

import java.util.stream.Stream;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacter.Race;

/**
 * Verify that assertions final methods or methods changing the object under test in {@link ObjectAssert} work with assumptions.
 */
class Object_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  public static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    return Stream.of(assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName)
                                                                .isEqualTo("Frodo"),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName)
                                                                .isEqualTo("Gandalf")),
                     assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, as(STRING))
                                                                .startsWith("Fro"),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, as(STRING))
                                                                .startsWith("Gan")),
                     assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting("name", "age")
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).extracting("name", "age")
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting("name")
                                                                .isEqualTo("Frodo"),
                                      value -> assumeThat(value).extracting("name")
                                                                .isEqualTo("Gandalf")),
                     assumptionRunner(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
                                      value -> assumeThat(value).extracting("name", as(STRING))
                                                                .startsWith("Fro"),
                                      value -> assumeThat(value).extracting("name", as(STRING))
                                                                .startsWith("Gan")));
  }

}
