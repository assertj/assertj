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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Assertions_sync_with_BDDAssumptions_Test extends BaseAssertionsTest {

  @Test
  void standard_assertions_and_bdd_assumptions_should_have_the_same_assertions_methods() {
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThat", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] givenMethods = findMethodsWithName(BDDAssumptions.class, "given");

    assertThat(givenMethods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                            .contains(assertThatMethods);
  }

  @Test
  void object_assertions_and_bdd_assumptions_should_have_the_same_assertions_methods() {
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThatObject", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] givenMethods = findMethodsWithName(BDDAssumptions.class, "givenObject");

    assertThat(givenMethods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                            .contains(assertThatMethods);
  }

  @Test
  void code_assertions_and_bdd_assumptions_should_have_the_same_assertions_methods() {
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThatCode", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] givenMethods = findMethodsWithName(BDDAssumptions.class, "givenCode");

    assertThat(givenMethods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                            .contains(assertThatMethods);
  }

  @ParameterizedTest
  @MethodSource("standard_and_bdd_assumption_methods")
  void standard_assertions_and_bdd_assumptions_should_have_the_same_assertions_methods(String assumptionMethod,
                                                                                       String bddAssumptionMethod) {
    // GIVEN
    Method[] assumeThat_Assertions_methods = findMethodsWithName(Assumptions.class, assumptionMethod);
    Method[] given_Assertions_methods = findMethodsWithName(BDDAssumptions.class, bddAssumptionMethod);
    // THEN
    given(given_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                  .containsExactlyInAnyOrder(assumeThat_Assertions_methods);
  }

  @ParameterizedTest
  @MethodSource("assumption_methods")
  void standard_assertions_and_with_assumptions_should_have_the_same_assertions_methods(String assumptionMethod) {
    // GIVEN
    Method[] assumeThat_Assertions_methods = findMethodsWithName(Assertions.class, assumptionMethod);
    Method[] assumeThat_WithAssertions_methods = findMethodsWithName(WithAssertions.class, assumptionMethod);
    // THEN
    given(assumeThat_WithAssertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                            .containsExactlyInAnyOrder(assumeThat_Assertions_methods);
  }

  @ParameterizedTest
  @MethodSource("assumption_methods")
  void standard_assertions_and_soft_assertions_should_have_the_same_assertions_methods(String assumptionMethod) {
    // GIVEN
    Method[] assumeThat_Assertions_methods = findMethodsWithName(Assertions.class, assumptionMethod,
                                                                 SPECIAL_IGNORED_RETURN_TYPES);
    Method[] assumeThat_SoftAssertions_methods = findMethodsWithName(StandardSoftAssertionsProvider.class, assumptionMethod);
    // THEN
    // ignore the return type of soft assertions until they have the same as the Assertions
    given(assumeThat_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                        .containsExactlyInAnyOrder(assumeThat_SoftAssertions_methods);
  }

  private static Stream<String> assumption_methods() {
    return Stream.of("assumeThat",
                     "assumeThatCollection",
                     "assumeThatIterable",
                     "assumeThatIterator",
                     "assumeThatList",
                     "assumeThatPath",
                     "assumeThatPredicate",
                     "assumeThatStream",
                     "assumeThatException",
                     "assumeThatRuntimeException",
                     "assumeThatNullPointerException",
                     "assumeThatIllegalArgumentException",
                     "assumeThatIOException",
                     "assumeThatIndexOutOfBoundsException",
                     "assumeThatReflectiveOperationException");
  }

  private static String toBDDAssumptionMethod(String assumptionMethod) {
    return assumptionMethod.replace("assumeThat", "given");
  }

  private static Stream<Arguments> standard_and_bdd_assumption_methods() {
    return assumption_methods().map(assumptionMethod -> arguments(assumptionMethod, toBDDAssumptionMethod(assumptionMethod)));
  }

}
