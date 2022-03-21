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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.TolkienCharacter.Race.DRAGON;
import static org.assertj.core.data.TolkienCharacter.Race.DWARF;
import static org.assertj.core.data.TolkienCharacter.Race.ELF;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.data.TolkienCharacter.Race.MAN;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AbstractAssert_satisfiesAnyOf_Test extends AbstractAssertBaseTest {

  private TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
  private TolkienCharacter legolas = TolkienCharacter.of("Legolas", 1000, ELF);
  private TolkienCharacter smaug = TolkienCharacter.of("Smaug", 171, DRAGON);
  private Consumer<TolkienCharacter> isHobbit = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(HOBBIT);
  private Consumer<TolkienCharacter> isElf = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(ELF);
  private Consumer<TolkienCharacter> isDwarf = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(DWARF);
  private Consumer<TolkienCharacter> isDragon = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(DRAGON);

  @Override
  protected ConcreteAssert invoke_api_method() {
    Consumer<Object> isZero = i -> assertThat(i).isNull();
    Consumer<Object> isNegative = i -> assertThat(i).isInstanceOf(String.class);
    expectAssertionError(() -> assertions.as("description").satisfiesAnyOf(isZero, isNegative));
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // checks that the code invoked in invoke_api_method called multipleAssertionsError with the correct parameters
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<AssertionError>> errors = ArgumentCaptor.forClass(List.class);
    verify(assertionErrorCreator).multipleAssertionsError(eq(new TextDescription("description")), errors.capture());
    assertThat(errors.getValue()).hasSize(2);
    assertThat(errors.getValue().get(0)).hasMessageContaining("null");
    assertThat(errors.getValue().get(1)).hasMessageContaining("to be an instance of");
  }

  @Override
  public void should_return_this() {
    // do nothing as we have invoked a failing assertion thus not returning anything.
  }

  @Test
  void should_pass_when_one_of_the_given_assertions_group_is_met() {
    assertThat(frodo).satisfiesAnyOf(isHobbit, isElf);
    assertThat(legolas).satisfiesAnyOf(isHobbit, isElf, isDwarf)
                       .satisfiesAnyOf(isHobbit, isElf);
    assertThat(smaug).satisfiesAnyOf(isHobbit, isElf, isDwarf, isDragon)
                     .satisfiesAnyOf(isHobbit, isDwarf, isDragon);
  }

  @Test
  void should_pass_when_all_of_the_given_assertions_groups_are_met() {
    // GIVEN
    Consumer<TolkienCharacter> namesStartsWithF = tolkienCharacter -> assertThat(tolkienCharacter.getName()).startsWith("F");
    // THEN
    assertThat(frodo).satisfiesAnyOf(isHobbit, namesStartsWithF, isHobbit);
  }

  @Test
  void should_pass_when_all_of_the_given_supertype_consumers_assertions_are_met() {
    // GIVEN
    Consumer<Object> notNullObject = object -> assertThat(object).isNotNull();
    Consumer<Object> isString = object -> assertThat(object).isInstanceOf(String.class);
    // WHEN/THEN
    then(frodo).satisfiesAnyOf(notNullObject, isString);
  }

  @Test
  void should_pass_with_mix_of_consumer_and_throwing_consumer() throws IOException {
    // GIVEN
    ThrowingConsumer<Reader> hasNotReachedEOF = reader -> assertThat(reader.read()).isPositive();
    Consumer<Object> notNullObject = object -> assertThat(object).isNotNull();
    // THEN
    then(new FileReader("src/test/resources/ascii.txt")).satisfiesAnyOf(hasNotReachedEOF, notNullObject);
  }

  @Test
  void should_fail_if_all_of_the_given_assertions_groups_fail() {
    // GIVEN
    TolkienCharacter boromir = TolkienCharacter.of("Boromir", 45, MAN);
    // WHEN/THEN
    expectAssertionError(() -> assertThat(boromir).as("description").satisfiesAnyOf(isHobbit, isElf));
  }

  @Test
  void should_throw_an_IllegalArgumentException_if_one_of_the_given_assertions_group_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(frodo).satisfiesAnyOf(isHobbit, null))
                                        .withMessage("No assertions group should be null");
  }

  @Test
  void should_honor_description() {
    // GIVEN
    Consumer<String> isEmpty = string -> assertThat(string).isEmpty();
    Consumer<String> endsWithZ = string -> assertThat(string).endsWith("Z");
    ThrowingCallable failingAssertionCode = () -> assertThat("abc").as("String checks").satisfiesAnyOf(isEmpty, endsWithZ);
    // WHEN
    AssertionError assertionError = expectAssertionError(failingAssertionCode);
    // THEN
    assertThat(assertionError).hasMessageContaining("String checks");
  }

  @Test
  void should_not_honor_overriding_error_message() {
    // GIVEN
    Consumer<String> isEmpty = string -> assertThat(string).overridingErrorMessage("fail empty").isEmpty();
    Consumer<String> endsWithZ = string -> assertThat(string).endsWith("Z");
    ThrowingCallable failingAssertionCode = () -> assertThat("abc").satisfiesAnyOf(isEmpty, endsWithZ);
    // THEN
    AssertionError assertionError = expectAssertionError(failingAssertionCode);
    // THEN
    assertThat(assertionError).hasMessageContaining("fail empty");
  }

}
