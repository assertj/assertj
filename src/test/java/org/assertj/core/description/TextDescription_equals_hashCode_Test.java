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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link TextDescription#equals(Object)}</code> and <code>{@link TextDescription#hashCode()}</code>.
 * 
 * @author Alex Ruiz
 */
public class TextDescription_equals_hashCode_Test {

  private static TextDescription description;

  @BeforeAll
  public static void setUpOnce() {
    description = new TextDescription("Yoda");
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(description);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(description, new TextDescription("Yoda"));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(description, new TextDescription("Yoda"), new TextDescription("Yoda"));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(description, new TextDescription("Yoda"));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(description.equals("Yoda")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(description.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_TextDescription_with_different_value() {
    assertThat(description.equals(new TextDescription("Luke"))).isFalse();
  }
}
