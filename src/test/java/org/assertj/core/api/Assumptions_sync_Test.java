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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Assumptions_sync_Test extends BaseAssertionsTest {

  @ParameterizedTest
  @CsvSource({
      "org.assertj.core.api.Assumptions, assumeThat",
      "org.assertj.core.api.BDDAssumptions, given",
  })
  void should_have_the_same_assumptions_public_methods(Class<?> target, String methodName) {
    // GIVEN
    Method[] assumeThat_Assumptions_methods = findMethodsWithName(Assumptions.class, "assumeThat");
    // THEN
    Method[] assumeThat_BDDAssumptions_methods = findMethodsWithName(target, methodName);
    // THEN
    then(assumeThat_BDDAssumptions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                           .containsExactlyInAnyOrder(assumeThat_Assumptions_methods);
  }

  @ParameterizedTest
  @CsvSource({
      "org.assertj.core.api.Assumptions, assume",
      "org.assertj.core.api.BDDAssumptions, given",
  })
  void should_have_the_same_non_assumptions_public_methods(Class<?> target, String excludePrefix) {
    // GIVEN
    Set<Method> expected_methods = findMethodsWithNameStartingWithout(Assumptions.class, "assume");
    // WHEN
    Set<Method> methods = findMethodsWithNameStartingWithout(target, excludePrefix);
    // THEN
    then(methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                 .containsExactlyInAnyOrderElementsOf(expected_methods);
  }

  private static Set<Method> findMethodsWithNameStartingWithout(Class<?> clazz, String excludePrefix) {
    return stream(clazz.getMethods()).filter(method -> !method.getName().startsWith(excludePrefix)).collect(toSet());
  }

}
