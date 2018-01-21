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

import static org.assertj.core.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.test.CartoonCharacter;
import org.junit.Test;

public abstract class BaseAssumptionsRunnerTest {

  {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  protected static CartoonCharacter homer;
  protected static CartoonCharacter fred;
  protected static CartoonCharacter lisa;
  protected static CartoonCharacter maggie;
  protected static CartoonCharacter bart;
  protected static ThrowingExtractor<? super TolkienCharacter, ?, RuntimeException> throwingNameExtractor;
  protected static ThrowingExtractor<? super TolkienCharacter, ?, RuntimeException> throwingAgeExtractor;
  protected static Extractor<? super TolkienCharacter, ?> nameExtractor;
  protected static Extractor<? super TolkienCharacter, ?> ageExtractor;
  protected AssumptionRunner<?> assumptionRunner;

  public BaseAssumptionsRunnerTest(AssumptionRunner<?> assumptionRunner) {
    this.assumptionRunner = assumptionRunner;
  }

  public static void setupData() {
    bart = new CartoonCharacter("Bart Simpson");
    lisa = new CartoonCharacter("Lisa Simpson");
    maggie = new CartoonCharacter("Maggie Simpson");

    homer = new CartoonCharacter("Homer Simpson");
    homer.getChildren().add(bart);
    homer.getChildren().add(lisa);
    homer.getChildren().add(maggie);

    CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
    fred = new CartoonCharacter("Fred Flintstone");
    fred.getChildren().add(pebbles);

    throwingNameExtractor = TolkienCharacter::getName;
    throwingAgeExtractor = TolkienCharacter::getAge;
    nameExtractor = TolkienCharacter::getName;
    ageExtractor = TolkienCharacter::getAge;
  }

  @Test
  public void should_ignore_test_when_assumption_fails() {
    assumptionRunner.runFailingAssumption();
    fail("should not arrive here");
  }

  @Test
  public void should_run_test_when_assumption_passes() {
    assumptionRunner.runPassingAssumption();
    incrementRunTests();
  }

  protected abstract void incrementRunTests();

}