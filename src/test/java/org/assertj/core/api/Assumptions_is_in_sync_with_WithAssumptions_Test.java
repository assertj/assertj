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

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.Test;

class Assumptions_is_in_sync_with_WithAssumptions_Test extends BaseAssertionsTest {

  @Test
  void standard_assumptions_and_with_assumptions_should_have_the_same_assumptions_methods() {
    Method[] assumeThat_Assumptions_methods = findMethodsWithName(Assumptions.class, "assumeThat");
    Method[] assumeThat_WithAssumptions_methods = findMethodsWithName(WithAssumptions.class, "assumeThat");

    then(assumeThat_WithAssumptions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                            .containsExactlyInAnyOrder(assumeThat_Assumptions_methods);
  }

  @Test
  void standard_assumptions_and_with_assumptions_should_have_the_same_non_assumptions_methods() {

    Set<Method> non_assumeThat_public_Assumptions_methods = non_assumeThat_public_methodsOf(Assumptions.class.getDeclaredMethods());
    Set<Method> non_assumeThat_public_WithAssumptions_methods = non_assumeThat_public_methodsOf(WithAssumptions.class.getDeclaredMethods());

    then(non_assumeThat_public_WithAssumptions_methods).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                       .containsExactlyInAnyOrderElementsOf(non_assumeThat_public_Assumptions_methods);
  }

  private static Set<Method> non_assumeThat_public_methodsOf(Method[] declaredMethods) {
    return stream(declaredMethods).filter(method -> !method.getName().startsWith("assume"))
                                  .filter(method -> isPublic(method.getModifiers()))
                                  .collect(toSet());
  }

}
