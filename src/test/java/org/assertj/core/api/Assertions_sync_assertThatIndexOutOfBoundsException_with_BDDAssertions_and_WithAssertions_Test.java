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

class Assertions_sync_assertThatIndexOutOfBoundsException_with_BDDAssertions_and_WithAssertions_Test
    extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatIndexOutOfBoundsException_and_BDDAssertions_thenIndexOutOfBoundsException_should_be_the_same() {
    Method[] assertThatIndexOutOfBoundsExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                   "assertThatIndexOutOfBoundsException");
    Method[] thenIndexOutOfBoundsExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class,
                                                                                "thenIndexOutOfBoundsException");

    assertThat(thenIndexOutOfBoundsExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                                            .containsExactlyInAnyOrder(assertThatIndexOutOfBoundsExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatIndexOutOfBoundsException_and_WithAssertions_assertThatIndexOutOfBoundsException_should_be_the_same() {
    Method[] assertThatIndexOutOfBoundsExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                   "assertThatIndexOutOfBoundsException");
    Method[] assertThatIndexOutOfBoundsExceptionInWithAssertions = findMethodsWithName(WithAssertions.class,
                                                                                       "assertThatIndexOutOfBoundsException");

    assertThat(assertThatIndexOutOfBoundsExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                                   .containsExactlyInAnyOrder(assertThatIndexOutOfBoundsExceptionInAssertions);
  }
}
