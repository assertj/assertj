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
package org.assertj.core.api;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class Assertions_sync_assertThat_with_BDD_and_Soft_variants_Test extends BaseAssertionsTest {

  @Test
  void standard_assertions_and_bdd_assertions_should_have_the_same_assertions_methods() {
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, "assertThat");
    Method[] then_Assertions_methods = findMethodsWithName(BDDAssertions.class, "then");

    assertThat(then_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                       .containsExactlyInAnyOrder(assertThat_Assertions_methods);
  }

  @Test
  void standard_assertions_and_with_assertions_should_have_the_same_assertions_methods() {
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, "assertThat");
    Method[] assertThat_WithAssertions_methods = findMethodsWithName(WithAssertions.class, "assertThat");

    assertThat(assertThat_WithAssertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                 .containsExactlyInAnyOrder(assertThat_Assertions_methods);
  }

  @Test
  void standard_assertions_and_with_assertions_should_have_the_same_non_assertions_methods() {

    Set<Method> non_assertThat_Assertions_methods = non_assertThat_methodsOf(Assertions.class.getDeclaredMethods());
    Set<Method> non_assertThat_WithAssertions_methods = non_assertThat_methodsOf(WithAssertions.class.getDeclaredMethods());

    assertThat(non_assertThat_WithAssertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                     .containsExactlyInAnyOrderElementsOf(non_assertThat_Assertions_methods);
  }

  @Test
  void standard_assertions_and_bdd_assertions_should_have_the_same_non_assertions_methods() {

    List<String> methodsToIgnore = list("failBecauseExceptionWasNotThrown", "filter", "offset");
    Set<Method> non_assertThat_methods = non_assertThat_methodsOf(Assertions.class.getDeclaredMethods());
    non_assertThat_methods = removeMethods(non_assertThat_methods, methodsToIgnore);
    Set<Method> non_then_methods = non_then_methodsOf(BDDAssertions.class.getDeclaredMethods());
    non_then_methods = removeMethods(non_then_methods, methodsToIgnore);

    assertThat(non_then_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                .containsExactlyInAnyOrderElementsOf(non_assertThat_methods);
  }

  private static Set<Method> removeMethods(Set<Method> methods, List<String> methodsToRemove) {
    return methods.stream()
                  .filter(method -> !methodsToRemove.contains(method.getName()))
                  .collect(toSet());
  }

  @Test
  void standard_assertions_and_soft_assertions_should_have_the_same_assertions_methods() {
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] assertThat_Assertions_methods = findMethodsWithName(Assertions.class, "assertThat", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] assertThat_SoftAssertions_methods = findMethodsWithName(StandardSoftAssertionsProvider.class, "assertThat");

    // ignore the return type of soft assertions until they have the same as the Assertions
    assertThat(assertThat_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                             .containsExactlyInAnyOrder(assertThat_SoftAssertions_methods);
  }

  @Test
  void bdd_assertions_and_bdd_soft_assertions_should_have_the_same_assertions_methods() {
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] then_Assertions_methods = findMethodsWithName(BDDAssertions.class, "then", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] then_BDDSoftAssertions_methods = findMethodsWithName(BDDSoftAssertionsProvider.class, "then");

    // ignore the return type of soft assertions until they have the same as the Assertions
    assertThat(then_Assertions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                       .containsExactlyInAnyOrder(then_BDDSoftAssertions_methods);
  }

  private static Set<Method> non_assertThat_methodsOf(Method[] declaredMethods) {
    return stream(declaredMethods).filter(method -> !method.getName().startsWith("assert")).collect(toSet());
  }

  private static Set<Method> non_then_methodsOf(Method[] declaredMethods) {
    return stream(declaredMethods).filter(method -> !method.getName().startsWith("then")).collect(toSet());
  }

}
