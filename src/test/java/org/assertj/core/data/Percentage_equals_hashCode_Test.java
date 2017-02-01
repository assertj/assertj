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
package org.assertj.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link org.assertj.core.data.Percentage#equals(Object)} and
 * {@link org.assertj.core.data.Percentage#hashCode()}.
 *
 * @author Alexander Bischof
 */
public class Percentage_equals_hashCode_Test {
  private static Percentage percentage;

  @BeforeClass
  public static void setUpOnce() {
    percentage = withPercentage(8.0);
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(percentage);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(percentage, withPercentage(8.0));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(percentage, withPercentage(8.0), withPercentage(8.0));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(percentage, withPercentage(8.0));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(percentage.equals("8")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(percentage.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_Percentage_with_different_value() {
    assertThat(percentage.equals(withPercentage(6.0))).isFalse();
  }
}
