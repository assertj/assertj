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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.assertj.core.api.Java6Assertions;
import org.assertj.core.api.Java6BDDAssertions;
import org.assertj.core.api.Java6BDDSoftAssertionsProvider;
import org.assertj.core.api.Java6StandardSoftAssertionsProvider;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
@SuppressWarnings("deprecation")
class Java6Assertions_sync_assertThat_with_BDD_and_Soft_variants_Test extends BaseAssertionsTest {

  @Test
  void standard_assertions_and_bdd_assertions_should_have_the_same_assertions_methods() {
    Method[] assertThatMethods = BaseAssertionsTest.findMethodsWithName(Java6Assertions.class, "assertThat");
    Method[] thenMethods = BaseAssertionsTest.findMethodsWithName(Java6BDDAssertions.class, "then");

    assertThat(assertThatMethods).usingElementComparator(BaseAssertionsTest.IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                 .containsExactlyInAnyOrder(thenMethods);
  }

  @Test
  void standard_assertions_and_soft_assertions_should_have_the_same_assertions_methods() {
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] assertThatMethods = BaseAssertionsTest.findMethodsWithName(Java6Assertions.class, "assertThat",
                                                                        BaseAssertionsTest.SPECIAL_IGNORED_RETURN_TYPES);
    Method[] assertThatSoftMethods = BaseAssertionsTest.findMethodsWithName(Java6StandardSoftAssertionsProvider.class,
                                                                            "assertThat");

    // ignore the return type of soft assertions until they have the same as the Assertions
    assertThat(assertThatMethods).usingElementComparator(BaseAssertionsTest.IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                 .containsExactlyInAnyOrder(assertThatSoftMethods);

  }

  @Test
  void bdd_assertions_and_bdd_soft_assertions_should_have_the_same_assertions_methods() {
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] thenMethods = BaseAssertionsTest.findMethodsWithName(Java6BDDAssertions.class, "then",
                                                                  BaseAssertionsTest.SPECIAL_IGNORED_RETURN_TYPES);
    Method[] thenSoftMethods = BaseAssertionsTest.findMethodsWithName(Java6BDDSoftAssertionsProvider.class, "then");

    // ignore the return type of soft assertions until they have the same as the Assertions
    assertThat(thenMethods).usingElementComparator(BaseAssertionsTest.IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                           .containsExactlyInAnyOrder(thenSoftMethods);

  }
}
