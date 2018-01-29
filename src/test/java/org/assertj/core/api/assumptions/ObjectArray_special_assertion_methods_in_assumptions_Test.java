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
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.run;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ProxyableObjectArrayAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.test.CartoonCharacter;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * verify that assertions final methods or methods changing the object under test in {@link ObjectArrayAssert} work with assumptions 
 * (i.e. that they are proxied correctly in {@link ProxyableObjectArrayAssert}).
 */
@RunWith(Parameterized.class)
public class ObjectArray_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  private static int ranTests = 0;

  public ObjectArray_special_assertion_methods_in_assumptions_Test(AssumptionRunner<?> assumptionRunner) {
    super(assumptionRunner);
  }

  @Override
  protected void incrementRunTests() {
    ranTests++;
  }

  @SuppressWarnings("unchecked")
  @Parameters
  public static Object[][] provideAssumptionsRunners() {
    setupData();
    return new AssumptionRunner[][] {
        // extracting methods
        run(array(frodo, sam),
            value -> assumeThat(value).extracting(throwingNameExtractor)
                                      .contains("Frodo"),
            value -> assumeThat(value).extracting(throwingNameExtractor)
                                      .contains("Gandalf")),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting(nameExtractor)
                                      .contains("Frodo", "Sam"),
            value -> assumeThat(value).extracting(nameExtractor)
                                      .contains("Gandalf", "Sam")),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting("name")
                                      .contains("Frodo", "Sam"),
            value -> assumeThat(value).extracting("name")
                                      .contains("Gandalf", "Sam")),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting("name", String.class)
                                      .contains("Frodo", "Sam"),
            value -> assumeThat(value).extracting("name", String.class)
                                      .contains("Gandalf", "Sam")),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains(tuple("Frodo", 33)),
            value -> assumeThat(value).extracting("name", "age")
                                      .contains(tuple("Gandalf", 1000))),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting(nameExtractorFunction, ageExtractorFunction)
                                      .contains(tuple("Frodo", 33)),
            value -> assumeThat(value).extracting(nameExtractorFunction, ageExtractorFunction)
                                      .contains(tuple("Gandalf", 1000))),
        run(array(frodo, sam),
            value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                      .contains(tuple("Frodo", 33)),
            value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                      .contains(tuple("Gandalf", 1000))),
        // extractingResultOf methods
        run(array(frodo, sam),
            value -> assumeThat(value).extractingResultOf("getName")
                                      .contains("Frodo", "Sam"),
            value -> assumeThat(value).extractingResultOf("getName")
                                      .contains("Gandalf", "Sam")),
        run(array(frodo, sam),
            value -> assumeThat(value).extractingResultOf("getName", String.class)
                                      .contains("Frodo", "Sam"),
            value -> assumeThat(value).extractingResultOf("getName", String.class)
                                      .contains("Gandalf", "Sam")),
        // flatExtracting methods
        run(array(homer, fred),
            value -> assumeThat(value).flatExtracting("children")
                                      .containsAnyOf(bart, lisa),
            value -> assumeThat(value).flatExtracting("children")
                                      .containsAnyOf(homer, fred)),
        run(array(homer, fred),
            value -> assumeThat(value).flatExtracting(childrenExtractor)
                                      .containsAnyOf(bart, lisa),
            value -> assumeThat(value).flatExtracting(childrenExtractor)
                                      .containsAnyOf(homer, fred)),
        run(array(homer, fred),
            value -> assumeThat(value).flatExtracting(CartoonCharacter::getChildren)
                                      .containsAnyOf(bart, lisa),
            value -> assumeThat(value).flatExtracting(CartoonCharacter::getChildren)
                                      .containsAnyOf(homer, fred)),
        // filteredOn methods
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                      .contains(frodo),
            value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                      .contains(sam)),
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOn(new Condition<>(hero -> hero.getName().startsWith("Fro"), "startsWith Fro"))
                                      .contains(frodo),
            value -> assumeThat(value).filteredOn(new Condition<>(hero -> hero.getName().startsWith("Fro"), "startsWith Fro"))
                                      .contains(sam)),
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOn("name", "Frodo")
                                      .contains(frodo),
            value -> assumeThat(value).filteredOn("name", "Frodo")
                                      .contains(sam)),
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOnNull("name")
                                      .isEmpty(),
            value -> assumeThat(value).filteredOnNull("name")
                                      .contains(sam)),
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOn("name", in("John", "Frodo"))
                                      .contains(frodo),
            value -> assumeThat(value).filteredOn("name", in("John", "Frodo"))
                                      .contains(sam)),
        run(array(frodo, sam),
            value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                      .extracting("name", "age")
                                      .contains(tuple("Frodo", 33)),
            value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                      .extracting("name", "age")
                                      .contains(tuple("Sam", 35))),
        run(array(1, 2, 3),
            value -> assumeThat(value).contains(1, 2),
            value -> assumeThat(value).contains(4)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsAnyOf(1, 10, 20),
            value -> assumeThat(value).containsAnyOf(0, 5, 10)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsExactly(1, 2, 3),
            value -> assumeThat(value).containsExactly(4)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsExactlyInAnyOrder(2, 1, 3),
            value -> assumeThat(value).containsExactlyInAnyOrder(1, 2)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsOnly(2, 1, 3, 2),
            value -> assumeThat(value).containsOnly(1, 2, 4)),
        run(array(2, 4, 2),
            value -> assumeThat(value).containsOnlyOnce(4),
            value -> assumeThat(value).containsOnlyOnce(2)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsSequence(1, 2),
            value -> assumeThat(value).containsSequence(1, 3)),
        run(array(1, 2, 3),
            value -> assumeThat(value).containsSubsequence(1, 3),
            value -> assumeThat(value).containsSubsequence(2, 1)),
        run(array(1, 2, 3),
            value -> assumeThat(value).doesNotContain(4, 5),
            value -> assumeThat(value).doesNotContain(2, 1)),
        run(array(1, 2, 3),
            value -> assumeThat(value).doesNotContainSequence(1, 3),
            value -> assumeThat(value).doesNotContainSequence(1, 2)),
        run(array(1, 2, 3),
            value -> assumeThat(value).doesNotContainSubsequence(2, 1),
            value -> assumeThat(value).doesNotContainSubsequence(1, 3)),
        run(array(1, 2, 3),
            value -> assumeThat(value).isSubsetOf(1, 2, 3, 4),
            value -> assumeThat(value).isSubsetOf(2, 4, 6)),
        run(array(1, 2, 3),
            value -> assumeThat(value).startsWith(1, 2),
            value -> assumeThat(value).startsWith(2, 3)),
        run(array(1, 2, 3),
            value -> assumeThat(value).endsWith(2, 3),
            value -> assumeThat(value).endsWith(2, 4))
    };
  };

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(provideAssumptionsRunners().length);
  }

}
