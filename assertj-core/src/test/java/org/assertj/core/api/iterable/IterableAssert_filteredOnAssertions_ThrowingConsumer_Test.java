/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.core.util.ThrowingConsumerFactory.throwingConsumer;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacterAssert;
import org.assertj.core.testkit.TolkienCharacterAssertFactory;
import org.junit.jupiter.api.Test;

class IterableAssert_filteredOnAssertions_ThrowingConsumer_Test extends IterableAssert_filtered_baseTest {

  private static final ThrowingConsumer<? super TolkienCharacter> nameStartingWithFro = hobbit -> assertThat(hobbit.getName()).startsWith("Fro");

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Throwable exception = new Throwable("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(employees).filteredOnAssertions(throwingConsumer(exception)));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .hasCauseReference(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(employees).filteredOnAssertions(throwingConsumer(runtimeException)));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_filter_iterable_under_test_verifying_given_assertions() {
    // GIVEN
    ThrowingConsumer<? super Employee> old = employee -> assertThat(employee.getAge()).isGreaterThan(100);
    // WHEN/THEN
    then(employees).filteredOnAssertions(old)
                   .containsOnly(yoda, obiwan);
    then(newHashSet(employees)).filteredOnAssertions(old)
                               .containsOnly(yoda, obiwan);
  }

  @Test
  void should_fail_if_given_consumer_is_null() {
    // GIVEN
    ThrowingConsumer<? super Employee> consumer = null;
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOnAssertions(consumer))
                                  .withMessage("The element assertions should not be null");
  }

  @Test
  void should_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnAssertions(nameStartingWithFro)
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnAssertions(nameStartingWithFro)
                                                      .last()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnAssertions(nameStartingWithFro)
                                                      .element(0)
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnAssertions(nameStartingWithFro)
                                                      .elements(0)
                                                      .first()
                                                      .hasAge(33);
  }

  @Test
  void should_honor_ClassBased_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    // THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnAssertions(nameStartingWithFro)
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnAssertions(nameStartingWithFro)
                                                     .last()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnAssertions(nameStartingWithFro)
                                                     .elements(0)
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnAssertions(nameStartingWithFro)
                                                     .element(0)
                                                     .hasAge(33);
  }

  @Test
  void should_keep_assertion_state() {
    // GIVEN
    Iterable<String> names = asList("John", "Doe", "Jane", "Doe");
    ThrowingConsumer<? super String> fourCharsWord = string -> assertThat(string.length()).isEqualTo(4);
    // WHEN
    IterableAssert<String> assertion = assertThat(names).as("test description")
                                                        .withFailMessage("error message")
                                                        .withRepresentation(UNICODE_REPRESENTATION)
                                                        .usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                        .filteredOnAssertions(fourCharsWord)
                                                        .containsExactly("JOHN", "JANE");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

}
