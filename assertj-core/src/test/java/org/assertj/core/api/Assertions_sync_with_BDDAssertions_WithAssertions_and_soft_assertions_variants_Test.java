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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Filip Hrisafov
 */
class Assertions_sync_with_BDDAssertions_WithAssertions_and_soft_assertions_variants_Test extends BaseAssertionsTest {

  // Assertions - BDDAssertions sync tests

  @ParameterizedTest
  @MethodSource("standard_and_bdd_assertion_methods")
  void standard_assertions_and_bdd_assertions_should_have_the_same_assertions_methods(String assertionMethod,
                                                                                      String bddAssertionMethod) {
    // GIVEN
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, assertionMethod);
    Method[] then_Assertions_methods = findMethodsWithName(BDDAssertions.class, bddAssertionMethod);
    // THEN
    then(then_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                 .containsExactlyInAnyOrder(assertThat_Assertions_methods);
  }

  @Test
  void standard_assertions_and_bdd_assertions_should_have_the_same_non_assertions_methods() {
    // GIVEN
    List<String> methodsToIgnore = list("failBecauseExceptionWasNotThrown", "filter", "offset");
    Set<Method> non_assertThat_methods = non_assertThat_methodsOf(Assertions.class.getDeclaredMethods());
    non_assertThat_methods = removeMethods(non_assertThat_methods, methodsToIgnore);
    Set<Method> non_then_methods = non_then_methodsOf(BDDAssertions.class.getDeclaredMethods());
    non_then_methods = removeMethods(non_then_methods, methodsToIgnore);
    // THEN
    then(non_then_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                          .containsExactlyInAnyOrderElementsOf(non_assertThat_methods);
  }

  // Assertions - WithAssertions sync tests

  @ParameterizedTest
  @MethodSource("assertion_methods")
  void standard_assertions_and_with_assertions_should_have_the_same_assertions_methods(String assertionMethod) {
    // GIVEN
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, assertionMethod);
    Method[] assertThat_WithAssertions_methods = findMethodsWithName(WithAssertions.class, assertionMethod);
    // THEN
    then(assertThat_WithAssertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                           .containsExactlyInAnyOrder(assertThat_Assertions_methods);
  }

  @Test
  void standard_assertions_and_with_assertions_should_have_the_same_non_assertions_methods() {
    // GIVEN
    Set<Method> non_assertThat_Assertions_methods = non_assertThat_methodsOf(Assertions.class.getDeclaredMethods());
    Set<Method> non_assertThat_WithAssertions_methods = non_assertThat_methodsOf(WithAssertions.class.getDeclaredMethods());
    // THEN
    then(non_assertThat_WithAssertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                               .containsExactlyInAnyOrderElementsOf(non_assertThat_Assertions_methods);
  }

  // Assertions - SoftAssertions sync tests

  @ParameterizedTest
  @MethodSource("assertion_methods")
  void standard_assertions_and_soft_assertions_should_have_the_same_assertions_methods(String assertionMethod) {
    // GIVEN
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, assertionMethod, SPECIAL_IGNORED_RETURN_TYPES);
    Method[] assertThat_SoftAssertions_methods = findMethodsWithName(StandardSoftAssertionsProvider.class, assertionMethod);
    // THEN
    // ignore the return type of soft assertions until they have the same as the Assertions
    then(assertThat_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                       .containsExactlyInAnyOrder(assertThat_SoftAssertions_methods);
  }

  // BDDAssertions - BDDSoftAssertions sync tests

  @ParameterizedTest
  @MethodSource("bdd_assertion_methods")
  void bdd_assertions_and_bdd_soft_assertions_should_have_the_same_assertions_methods(String assertionMethod) {
    // GIVEN
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] then_Assertions_methods = findMethodsWithName(BDDAssertions.class, assertionMethod, SPECIAL_IGNORED_RETURN_TYPES);
    Method[] then_BDDSoftAssertions_methods = findMethodsWithName(BDDSoftAssertionsProvider.class, assertionMethod);
    // THEN
    // ignore the return type of soft assertions until they have the same as the Assertions
    then(then_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                 .containsExactlyInAnyOrder(then_BDDSoftAssertions_methods);
  }

  private static Stream<String> assertion_methods() {
    return Stream.of("assertThat",
                     "assertThatCollection",
                     "assertThatComparable",
                     "assertThatIterable",
                     "assertThatIterator",
                     "assertThatList",
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

  private static String toBDDAssertionMethod(String assertionMethod) {
    return assertionMethod.replace("assertThat", "then");
  }

  private static Stream<String> bdd_assertion_methods() {
    return assertion_methods().map(assertionMethod -> toBDDAssertionMethod(assertionMethod));
  }

  private static Stream<Arguments> standard_and_bdd_assertion_methods() {
    return assertion_methods().map(assertionMethod -> arguments(assertionMethod, toBDDAssertionMethod(assertionMethod)));
  }

  private static Set<Method> non_assertThat_methodsOf(Method[] declaredMethods) {
    return stream(declaredMethods).filter(method -> !method.getName().startsWith("assert"))
                                  .filter(method -> !method.isSynthetic())
                                  .collect(toSet());
  }

  private static Set<Method> non_then_methodsOf(Method[] declaredMethods) {
    return stream(declaredMethods).filter(method -> !method.getName().startsWith("then"))
                                  .filter(method -> !method.isSynthetic())
                                  .collect(toSet());
  }

  private static Set<Method> removeMethods(Set<Method> methods, List<String> methodsToRemove) {
    return methods.stream()
                  .filter(method -> !methodsToRemove.contains(method.getName()))
                  .collect(toSet());
  }

}
