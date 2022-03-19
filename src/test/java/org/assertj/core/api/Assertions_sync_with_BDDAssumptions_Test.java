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

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

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
    Class<?>[] ignoredReturnTypes = ArrayUtils.addAll(SPECIAL_IGNORED_RETURN_TYPES, AbstractCallableAssert.class); // FIXME gh-xxxx
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThatCode", ignoredReturnTypes);
    Method[] givenMethods = findMethodsWithName(BDDAssumptions.class, "givenCode");

    assertThat(givenMethods).usingElementComparator(IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME)
                            .contains(assertThatMethods);
  }

}
