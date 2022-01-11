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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Assertions_sync_assertThatReflectiveOperationException_with_BDDAssertions_and_WithAssertions_Test
    extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatReflectiveOperationException_and_BDDAssertions_thenReflectiveOperationException_should_be_the_same() {
    Method[] assertThatReflectiveOperationExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                      "assertThatReflectiveOperationException");
    Method[] thenReflectiveOperationExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class,
                                                                                   "thenReflectiveOperationException");

    assertThat(thenReflectiveOperationExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                                               .containsExactlyInAnyOrder(assertThatReflectiveOperationExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatReflectiveOperationException_and_WithAssertions_assertThatReflectiveOperationException_should_be_the_same() {
    Method[] assertThatReflectiveOperationExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                      "assertThatReflectiveOperationException");
    Method[] assertThatReflectiveOperationExceptionInWithAssertions = findMethodsWithName(WithAssertions.class,
                                                                                          "assertThatReflectiveOperationException");

    assertThat(assertThatReflectiveOperationExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                                      .containsExactlyInAnyOrder(assertThatReflectiveOperationExceptionInAssertions);
  }
}
