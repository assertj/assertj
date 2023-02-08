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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.assumptionRunner;
import static org.assertj.core.util.IterableUtil.iterable;

import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.test.CartoonCharacter;

/**
 * verify that assertions final methods or methods changing the object under test in {@link IterableAssert} work with assumptions
 * (i.e. that they are proxied correctly in {@link ProxyableIterableAssert}).
 */
class Iterable_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  public static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    return Stream.of(
                     // extracting methods
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting(throwingNameExtractor)
                                                                .contains("Frodo"),
                                      value -> assumeThat(value).extracting(throwingNameExtractor)
                                                                .contains("Gandalf")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).map(throwingNameExtractor)
                                                                .contains("Frodo"),
                                      value -> assumeThat(value).map(throwingNameExtractor)
                                                                .contains("Gandalf")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting(nameExtractor)
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).extracting(nameExtractor)
                                                                .contains("Gandalf", "Sam")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).map(nameExtractor)
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).map(nameExtractor)
                                                                .contains("Gandalf", "Sam")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting("name")
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).extracting("name")
                                                                .contains("Gandalf", "Sam")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting("name", String.class)
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).extracting("name", String.class)
                                                                .contains("Gandalf", "Sam")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting("name", "age")
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).extracting("name", "age")
                                                                .contains(tuple("Gandalf", 1000))),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting(nameExtractorFunction, ageExtractorFunction)
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).extracting(nameExtractorFunction, ageExtractorFunction)
                                                                .contains(tuple("Gandalf", 1000))),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).map(nameExtractorFunction, ageExtractorFunction)
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).map(nameExtractorFunction, ageExtractorFunction)
                                                                .contains(tuple("Gandalf", 1000))),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).extracting(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains(tuple("Gandalf", 1000))),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).map(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).map(TolkienCharacter::getName, TolkienCharacter::getAge)
                                                                .contains(tuple("Gandalf", 1000))),
                     // extractingResultOf methods
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extractingResultOf("getName")
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).extractingResultOf("getName")
                                                                .contains("Gandalf", "Sam")),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).extractingResultOf("getName", String.class)
                                                                .contains("Frodo", "Sam"),
                                      value -> assumeThat(value).extractingResultOf("getName", String.class)
                                                                .contains("Gandalf", "Sam")),
                     // flatExtracting methods
                     assumptionRunner(iterable(homer, fred),
                                      value -> assumeThat(value).flatExtracting("children")
                                                                .containsAnyOf(bart, lisa),
                                      value -> assumeThat(value).flatExtracting("children")
                                                                .containsAnyOf(homer, fred)),
                     assumptionRunner(iterable(homer, fred),
                                      value -> assumeThat(value).flatExtracting(CartoonCharacter::getChildren)
                                                                .containsAnyOf(bart, lisa),
                                      value -> assumeThat(value).flatExtracting(CartoonCharacter::getChildren)
                                                                .containsAnyOf(homer, fred)),
                     assumptionRunner(iterable(homer, fred),
                                      value -> assumeThat(value).flatMap(CartoonCharacter::getChildren)
                                                                .containsAnyOf(bart, lisa),
                                      value -> assumeThat(value).flatMap(CartoonCharacter::getChildren)
                                                                .containsAnyOf(homer, fred)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).flatExtracting(throwingNameExtractor, throwingAgeExtractor)
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).flatExtracting(throwingNameExtractor, throwingAgeExtractor)
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).flatExtracting(nameExtractor, ageExtractor)
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).flatExtracting(nameExtractor, ageExtractor)
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).flatMap(throwingNameExtractor, throwingAgeExtractor)
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).flatMap(throwingNameExtractor, throwingAgeExtractor)
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).flatMap(nameExtractor, ageExtractor)
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).flatMap(nameExtractor, ageExtractor)
                                                                .contains("Gandalf", 1000)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).flatExtracting("name", "age")
                                                                .contains("Frodo", 33),
                                      value -> assumeThat(value).flatExtracting("name", "age")
                                                                .contains("Gandalf", 1000)),
                     // filteredOn methods
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                                                .contains(frodo),
                                      value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn(new Condition<>(hero -> hero.getName()
                                                                                                        .startsWith("Fro"),
                                                                                            "startsWith Fro"))
                                                                .contains(frodo),
                                      value -> assumeThat(value).filteredOn(new Condition<>(hero -> hero.getName()
                                                                                                        .startsWith("Fro"),
                                                                                            "startsWith Fro"))
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOnAssertions(hero -> assertThat(hero.getName()).startsWith("Fro"))
                                                                .contains(frodo),
                                      value -> assumeThat(value).filteredOnAssertions(hero -> assertThat(hero.getName()).startsWith("Fro"))
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn("name", "Frodo")
                                                                .contains(frodo),
                                      value -> assumeThat(value).filteredOn("name", "Frodo")
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOnNull("name")
                                                                .isEmpty(),
                                      value -> assumeThat(value).filteredOnNull("name")
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn("name", in("John", "Frodo"))
                                                                .contains(frodo),
                                      value -> assumeThat(value).filteredOn("name", in("John", "Frodo"))
                                                                .contains(sam)),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                                                .extracting("name", "age")
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).filteredOn(hero -> hero.getName().startsWith("Fro"))
                                                                .extracting("name", "age")
                                                                .contains(tuple("Sam", 35))),
                     assumptionRunner(iterable(frodo, sam),
                                      value -> assumeThat(value).filteredOn(TolkienCharacter::getName, "Frodo")
                                                                .extracting("name", "age")
                                                                .contains(tuple("Frodo", 33)),
                                      value -> assumeThat(value).filteredOn(hero -> hero.getName(), "Frodo")
                                                                .extracting("name", "age")
                                                                .contains(tuple("Sam", 35))),
                     // final methods
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).contains(1, 2),
                                      value -> assumeThat(value).contains(4)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsAnyOf(1, 10, 20),
                                      value -> assumeThat(value).containsAnyOf(0, 5, 10)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsExactly(1, 2, 3),
                                      value -> assumeThat(value).containsExactly(4)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsExactlyInAnyOrder(2, 1, 3),
                                      value -> assumeThat(value).containsExactlyInAnyOrder(1, 2)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsOnly(2, 1, 3, 2),
                                      value -> assumeThat(value).containsOnly(1, 2, 4)),
                     assumptionRunner(iterable(2, 4, 2),
                                      value -> assumeThat(value).containsOnlyOnce(4),
                                      value -> assumeThat(value).containsOnlyOnce(2)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsSequence(1, 2),
                                      value -> assumeThat(value).containsSequence(1, 3)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).containsSubsequence(1, 3),
                                      value -> assumeThat(value).containsSubsequence(2, 1)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).doesNotContain(4, 5),
                                      value -> assumeThat(value).doesNotContain(2, 1)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).doesNotContainSequence(1, 3),
                                      value -> assumeThat(value).doesNotContainSequence(1, 2)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).doesNotContainSubsequence(2, 1),
                                      value -> assumeThat(value).doesNotContainSubsequence(1, 3)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).isSubsetOf(1, 2, 3, 4),
                                      value -> assumeThat(value).isSubsetOf(2, 4, 6)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).startsWith(1, 2),
                                      value -> assumeThat(value).startsWith(2, 3)),
                     assumptionRunner(iterable(1, 2, 3),
                                      value -> assumeThat(value).endsWith(2, 3),
                                      value -> assumeThat(value).endsWith(2, 4)),
                     assumptionRunner(iterable(1, 2),
                                      value -> assumeThat(value).satisfiesExactly(i -> assertThat(i).isOdd(),
                                                                                  i -> assertThat(i).isEven()),
                                      value -> assumeThat(value).satisfiesExactly(i -> assertThat(i).isOdd(),
                                                                                  i -> assertThat(i).isOdd())),
                     assumptionRunner(iterable(1, 2),
                                      value -> assumeThat(value).satisfiesExactlyInAnyOrder(i -> assertThat(i).isEven(),
                                                                                            i -> assertThat(i).isOdd()),
                                      value -> assumeThat(value).satisfiesExactlyInAnyOrder(i -> assertThat(i).isOdd(),
                                                                                            i -> assertThat(i).isOdd())),
                     assumptionRunner(iterable(1, 2),
                                      value -> assumeThat(value).satisfiesOnlyOnce(i -> assertThat(i).isEven()),
                                      value -> assumeThat(value).satisfiesOnlyOnce(i -> assertThat(i).isGreaterThan(0))));
  }

}
