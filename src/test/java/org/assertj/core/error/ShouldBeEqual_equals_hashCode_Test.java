/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.EqualsHashCodeContractAssert.*;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.*;

/**
 * Tests for <code>{@link ShouldBeEqual#equals(Object)}</code> and <code>{@link ShouldBeEqual#hashCode()}</code>.
 * 
 * @author Yvonne Wang
 */
public class ShouldBeEqual_equals_hashCode_Test {

  private static ShouldBeEqual factory;

  @BeforeClass
  public static void setUpOnce() {
    factory = (ShouldBeEqual) shouldBeEqual("Yoda", "Luke", new StandardRepresentation());
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(factory);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(factory, shouldBeEqual("Yoda", "Luke", new StandardRepresentation()));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(factory, shouldBeEqual("Yoda", "Luke", new StandardRepresentation()),
        shouldBeEqual("Yoda", "Luke", new StandardRepresentation()));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(factory, shouldBeEqual("Yoda", "Luke", new StandardRepresentation()));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(factory.equals("Yoda")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(factory.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_IsNotEqual_with_different_actual() {
    assertThat(factory.equals(shouldBeEqual("Leia", "Luke", new StandardRepresentation()))).isFalse();
  }

  @Test
  public void should_not_be_equal_to_IsNotEqual_with_different_expected() {
    assertThat(factory.equals(shouldBeEqual("Yoda", "Leia", new StandardRepresentation()))).isFalse();
  }
}
