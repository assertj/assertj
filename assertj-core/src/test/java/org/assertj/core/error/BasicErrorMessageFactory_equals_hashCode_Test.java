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
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BasicErrorMessageFactory#equals(Object)}</code> and
 * <code>{@link BasicErrorMessageFactory#hashCode()}</code>.
 * 
 * @author Yvonne Wang
 */
class BasicErrorMessageFactory_equals_hashCode_Test {

  private static BasicErrorMessageFactory factory;

  @BeforeAll
  public static void setUpOnce() {
    factory = new BasicErrorMessageFactory("Hello %s", "Yoda");
  }

  @Test
  void should_have_reflexive_equals() {
    assertEqualsIsReflexive(factory);
  }

  @Test
  void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(factory, new BasicErrorMessageFactory("Hello %s", "Yoda"));
  }

  @Test
  void should_have_transitive_equals() {
    BasicErrorMessageFactory obj2 = new BasicErrorMessageFactory("Hello %s", "Yoda");
    BasicErrorMessageFactory obj3 = new BasicErrorMessageFactory("Hello %s", "Yoda");
    assertEqualsIsTransitive(factory, obj2, obj3);
  }

  @Test
  void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(factory, new BasicErrorMessageFactory("Hello %s", "Yoda"));
  }

  @Test
  void should_not_be_equal_to_Object_of_different_type() {
    then(factory.equals("Yoda")).isFalse();
  }

  @Test
  void should_not_be_equal_to_null() {
    then(factory.equals(null)).isFalse();
  }

  @Test
  void should_not_be_equal_to_BasicErrorMessage_with_different_format() {
    then(factory.equals(new BasicErrorMessageFactory("How are you, %s?", "Yoda"))).isFalse();
  }

  @Test
  void should_not_be_equal_to_BasicErrorMessage_with_different_arguments() {
    then(factory.equals(new BasicErrorMessageFactory("Hello %s", "Luke"))).isFalse();
  }
}
