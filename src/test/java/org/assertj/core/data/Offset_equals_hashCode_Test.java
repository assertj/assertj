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
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.test.EqualsHashCodeContractAssert.*;


import org.assertj.core.data.Offset;
import org.junit.*;

/**
 * Tests for {@link Offset#equals(Object)} and {@link Offset#hashCode()}.
 *
 * @author Alex Ruiz
 */
public class Offset_equals_hashCode_Test {
  private static Offset<Integer> offset;

  @BeforeClass
  public static void setUpOnce() {
    offset = offset(8);
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(offset);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(offset, offset(8));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(offset, offset(8), offset(8));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(offset, offset(8));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(offset.equals("8")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(offset.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_Offset_with_different_value() {
    assertThat(offset.equals(offset(6))).isFalse();
  }
}
