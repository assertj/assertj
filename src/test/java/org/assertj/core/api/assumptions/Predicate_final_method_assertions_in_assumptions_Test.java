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

import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.assumptionRunner;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ProxyableClassAssert;
import org.assertj.core.data.MapEntry;

/**
 * verify that assertions final methods in {@link ClassAssert} work with assumptions (i.e. that they are proxied correctly in {@link ProxyableClassAssert}).
 */
public class Predicate_final_method_assertions_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  public static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    Predicate<MapEntry<String, String>> ballSportPredicate = sport -> sport.value.contains("ball");
    return Stream.of(
        assumptionRunner(ballSportPredicate,
            value -> assumeThat(value).accepts(entry("sport", "football"), entry("sport", "basketball")),
            value -> assumeThat(value).accepts(entry("sport", "boxing"), entry("sport", "marathon"))),
        assumptionRunner(ballSportPredicate,
            value -> assumeThat(value).rejects(entry("sport", "boxing"), entry("sport", "marathon")),
            value -> assumeThat(value).rejects(entry("sport", "football"), entry("sport", "basketball")))
    );
  }
}
