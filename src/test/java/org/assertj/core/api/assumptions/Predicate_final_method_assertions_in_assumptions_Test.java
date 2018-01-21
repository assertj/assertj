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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.run;

import java.util.function.Predicate;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ProxyableClassAssert;
import org.assertj.core.data.MapEntry;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * verify that assertions final methods in {@link ClassAssert} work with assumptions (i.e. that they are proxied correctly in {@link ProxyableClassAssert}).
 */
@RunWith(Parameterized.class)
public class Predicate_final_method_assertions_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  private static int ranTests = 0;

  public Predicate_final_method_assertions_in_assumptions_Test(AssumptionRunner<?> assumptionRunner) {
    super(assumptionRunner);
  }

  @Override
  protected void incrementRunTests() {
    ranTests++;
  }

  @SuppressWarnings("unchecked")
  @Parameters
  public static Object[][] provideAssumptionsRunners() {
    Predicate<MapEntry<String, String>> ballSportPredicate = sport -> sport.value.contains("ball");
    return new AssumptionRunner[][] {
        run(ballSportPredicate,
            value -> assumeThat(value).accepts(entry("sport", "football"), entry("sport", "basketball")),
            value -> assumeThat(value).accepts(entry("sport", "boxing"), entry("sport", "marathon"))),
        run(ballSportPredicate,
            value -> assumeThat(value).rejects(entry("sport", "boxing"), entry("sport", "marathon")),
            value -> assumeThat(value).rejects(entry("sport", "football"), entry("sport", "basketball")))
    };
  };

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(provideAssumptionsRunners().length);
  }

}
