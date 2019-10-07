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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.assumptionRunner;

import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.DisplayName;

/**
 * Verify that assertions changing the object under test in {@link OptionalAssert} work with assumptions.
 */
@DisplayName("Optional special assertion methods in assumptions")
class Optional_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    Optional<String> optional = Optional.of("Thor");

    return Stream.of(assumptionRunner(optional,
                                      value -> assumeThat(value).get().isEqualTo("Thor"),
                                      value -> assumeThat(value).get().isEqualTo("Hulk")),
                     assumptionRunner(optional,
                                      value -> assumeThat(value).get(as(STRING)).startsWith("Tho"),
                                      value -> assumeThat(value).get(as(STRING)).startsWith("Hul")));
  }

}
