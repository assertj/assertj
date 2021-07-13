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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.condition.MappedCondition.mappedCondition;

import java.util.Optional;
import java.util.function.Supplier;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MappedConditionTest {

  private static final String INNER_CONDITION_DESCRIPTION = "isString and BAR";

  private static final String BAR = "bar";

  private static final String FOO = "foo";

  private final static Condition<String> isBarString = new Condition<>(s -> BAR.equals(s), INNER_CONDITION_DESCRIPTION);

  private final static String BAR_CONDITION_DESCRIPTION = format("mapped%n" +
                                                                 "   using: ::toString%n" +
                                                                 "   from: <StringBuilder> " + BAR + "%n" +
                                                                 "   to:   <String> " + BAR + "%n" +
                                                                 "   then checked:%n" +
                                                                 "      " + INNER_CONDITION_DESCRIPTION);

  private final static String BAR_CONDITION_DESCRIPTION_PLAIN = format("mapped%n" +
                                                                       "   from: <StringBuilder> " + BAR + "%n" +
                                                                       "   to:   <String> " + BAR + "%n" +
                                                                       "   then checked:%n" +
                                                                       "      " + INNER_CONDITION_DESCRIPTION);

  private final static String FOO_CONDITION_DESCRIPTION = format("mapped%n" +
                                                                 "   using: ::toString%n" +
                                                                 "   from: <StringBuilder> " + FOO + "%n" +
                                                                 "   to:   <String> " + FOO + "%n" +
                                                                 "   then checked:%n" +
                                                                 "      " + INNER_CONDITION_DESCRIPTION);

  @Test
  void mappedCondition_withDescription_works() {
    // WHEN
    Condition<StringBuilder> mappedCondition = mappedCondition(StringBuilder::toString, isBarString, "%stoString", "::");
    // THEN
    then(mappedCondition.matches(new StringBuilder(BAR))).isTrue();
    then(mappedCondition).hasToString(BAR_CONDITION_DESCRIPTION);
    then(mappedCondition.matches(new StringBuilder(FOO))).isFalse();
    then(mappedCondition).hasToString(FOO_CONDITION_DESCRIPTION);
  }

  @Test
  void mappedCondition_withoutDescription_works() {
    // WHEN
    Condition<StringBuilder> mappedCondition = mappedCondition(StringBuilder::toString, isBarString);
    // THEN
    then(mappedCondition.matches(new StringBuilder(BAR))).isTrue();
    then(mappedCondition).hasToString(BAR_CONDITION_DESCRIPTION_PLAIN);
  }

  @Test
  void mappedCondition_with_description_and_null_condition_should_throw_NPE() {
    // GIVEN
    Condition<String> nullCondition = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> mappedCondition(StringBuilder::toString, nullCondition, "::toString"))
                              .withMessage("The given condition should not be null");
  }

  @Test
  void mappedCondition_with_description_and_null_mapping_function_should_throw_NPE() {
    thenNullPointerException().isThrownBy(() -> mappedCondition(null, isBarString, "::toString"))
                              .withMessage("The given mapping function should not be null");
  }

  @Test
  void mappedCondition_without_description_and_null_condition_should_throw_NPE() {
    // GIVEN
    Condition<String> nullCondition = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> mappedCondition(StringBuilder::toString, nullCondition))
                              .withMessage("The given condition should not be null");
  }

  @Test
  void mappedCondition_without_description_and_null_mapping_function_should_throw_NPE() {
    thenNullPointerException().isThrownBy(() -> mappedCondition(null, isBarString))
                              .withMessage("The given mapping function should not be null");
  }

  @Test
  void mappedCondition_with_null_description_and_should_throw_NPE() {
    // GIVEN
    String nullDescription = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> mappedCondition(StringBuilder::toString, isBarString, nullDescription))
                              .withMessage("The given mappingDescription should not be null");
  }

  @Test
  void mappedCondition_with_null_supplyer_function_should_print_null() {
   MappedCondition<Supplier<Object>, Object> mapped= mappedCondition( Supplier::get,new Condition<>(o->o==null,"is null"), "Supplier::get");
   boolean b= mapped.matches(()->null);
   then(b).isTrue();
   then(mapped.toString()).contains("to:   <null> null");
  }

  @Test
  void example() {
    // GIVEN
    Condition<String> hasLineSeparator = new Condition<>(text -> text.contains(lineSeparator()), "has lineSeparator");
    Optional<String> optionalString = Optional.of("a" + lineSeparator());
    // WHEN
    Condition<Optional<String>> mappedCondition = mappedCondition(Optional<String>::get, hasLineSeparator);
    boolean matches = mappedCondition.matches(optionalString);
    // THEN
    then(matches).isTrue();
  }
}
