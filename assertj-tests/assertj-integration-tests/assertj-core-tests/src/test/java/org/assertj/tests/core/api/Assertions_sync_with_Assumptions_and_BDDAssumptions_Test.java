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
package org.assertj.tests.core.api;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.assertj.core.api.BDDAssumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Assertions_sync_with_Assumptions_and_BDDAssumptions_Test extends BaseAssertionsTest {

  @ParameterizedTest
  @MethodSource("assertion_and_assumption_methods")
  void standard_assertions_and_assumptions_should_have_similar_methods(String assertionMethod, String assumptionMethod) {
    // GIVEN
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, assertionMethod);
    Method[] assumeThat_Assumptions_methods = findMethodsWithName(Assumptions.class, assumptionMethod);
    // THEN
    assumeThat(assumeThat_Assumptions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                                              .containsExactlyInAnyOrder(assertThat_Assertions_methods);
  }

  @ParameterizedTest
  @MethodSource("standard_and_bdd_assumption_methods")
  void standard_assumptions_and_bdd_assumptions_should_have_similar_methods(String assumptionMethod, String bddAssumptionMethod) {
    // GIVEN
    Method[] assumeThat_Assumptions_methods = findMethodsWithName(Assumptions.class, assumptionMethod);
    Method[] given_Assumptions_methods = findMethodsWithName(BDDAssumptions.class, bddAssumptionMethod);
    // THEN
    given(given_Assumptions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                    .containsExactlyInAnyOrder(assumeThat_Assumptions_methods);
  }

  @Test
  void assertThat_and_assumeThat_should_be_similar_in_standard_assertions_and_assumptions() {
    // GIVEN
    // if we wanted the exact same assertThat/assumeThat in Assertions/Assumptions, we would add "assertThat" in assertion_methods
    // but there are some methods that are not that valuable to add in Assumptions, these are specified in shouldBeIncludedInTest
    Method[] assumeThat_methods = findMethodsWithName(Assumptions.class, "assumeThat");
    Method[] assertThat_methods = findMethodsWithName(Assertions.class, "assertThat");
    Method[] filtered_assertThat_methods = stream(assertThat_methods).filter(method -> shouldBeIncludedInTest(method))
                                                                     .toArray(Method[]::new);
    // THEN
    assumeThat(assumeThat_methods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                                  .containsExactlyInAnyOrder(filtered_assertThat_methods);
  }

  private static Stream<String> assertion_methods() {
    return Stream.of("assertThatCode",
                     "assertThatCollection",
                     "assertThatComparable",
                     "assertThatIterable",
                     "assertThatIterator",
                     "assertThatList",
                     "assertThatObject",
                     "assertThatPath",
                     "assertThatPredicate",
                     "assertThatStream",
                     "assertThatException",
                     "assertThatRuntimeException",
                     "assertThatNullPointerException",
                     "assertThatIllegalArgumentException",
                     "assertThatIOException",
                     "assertThatIndexOutOfBoundsException",
                     "assertThatReflectiveOperationException");
  }

  private static boolean shouldBeIncludedInTest(Method methodToTest) {
    // these are methods that are rarely used and can be (partially) addressed with InstanceOfAssertFactories.
    Stream<String> methodsToIgnore = Stream.of("public static java.lang.Object org.assertj.core.api.Assertions.assertThat(org.assertj.core.api.AssertProvider)",
                                               "public static org.assertj.core.api.ClassBasedNavigableListAssert org.assertj.core.api.Assertions.assertThat(java.util.List,java.lang.Class)",
                                               "public static org.assertj.core.api.FactoryBasedNavigableIterableAssert org.assertj.core.api.Assertions.assertThat(java.lang.Iterable,org.assertj.core.api.AssertFactory)",
                                               "public static org.assertj.core.api.ClassBasedNavigableIterableAssert org.assertj.core.api.Assertions.assertThat(java.lang.Iterable,java.lang.Class)",
                                               "public static org.assertj.core.api.FactoryBasedNavigableListAssert org.assertj.core.api.Assertions.assertThat(java.util.List,org.assertj.core.api.AssertFactory)");
    String methodDescription = methodToTest.toString();
    return methodsToIgnore.noneMatch(methodToIgnore -> methodDescription.equals(methodToIgnore));
  }

  private static String toBDDAssumptionMethod(String assumptionMethod) {
    return assumptionMethod.replace("assumeThat", "given");
  }

  private static String toAssumptionMethod(String assertionMethod) {
    return assertionMethod.replace("assertThat", "assumeThat");
  }

  private static Stream<String> assumption_methods() {
    return assertion_methods().map(assertionMethod -> toAssumptionMethod(assertionMethod));
  }

  private static Stream<Arguments> standard_and_bdd_assumption_methods() {
    return assumption_methods().map(assumptionMethod -> arguments(assumptionMethod, toBDDAssumptionMethod(assumptionMethod)));
  }

  private static Stream<Arguments> assertion_and_assumption_methods() {
    return assertion_methods().map(assertionMethod -> arguments(assertionMethod, toAssumptionMethod(assertionMethod)));
  }

}
