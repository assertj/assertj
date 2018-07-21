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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.run;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ProxyableObjectAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacter.Race;

/**
 * verify that assertions final methods or methods changing the object under test in {@link ObjectAssert} work with assumptions
 * (i.e. that they are proxied correctly in {@link ProxyableObjectAssert}).
 */
public class Object_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {;

  @SuppressWarnings("unchecked")
  public static Object[][] provideAssumptionsRunners() {
    return new AssumptionRunner[][] {
        run(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
            value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                      .contains("Frodo", 33),
            value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                      .contains("Gandalf", 1000)),
        run(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
            value -> assumeThat(value).extracting(TolkienCharacter::getName)
                                      .isEqualTo("Frodo"),
            value -> assumeThat(value).extracting(TolkienCharacter::getName)
                                      .isEqualTo("Gandalf")),
        run(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains("Frodo", 33),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains("Gandalf", 1000)),
        run(TolkienCharacter.of("Frodo", 33, Race.HOBBIT),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains("Frodo", 33),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains("Gandalf", 1000))
    };
  };
}
