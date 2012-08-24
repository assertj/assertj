/*
 * Created on Oct 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertFalse;
import static org.fest.assertions.data.Index.atIndex;
import static org.fest.test.EqualsHashCodeContractAssert.*;

import org.junit.*;

/**
 * Tests for {@link Index#equals(Object)} and {@link Index#hashCode()}.
 *
 * @author Alex Ruiz
 */
public class Index_equals_hashCode_Test {
  private static Index index;

  @BeforeClass
  public static void setUpOnce() {
    index = atIndex(8);
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(index);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(index, atIndex(8));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(index, atIndex(8), atIndex(8));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(index, atIndex(8));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(index.equals("8"));
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertFalse(index.equals(null));
  }

  @Test
  public void should_not_be_equal_to_Index_with_different_value() {
    assertFalse(index.equals(atIndex(6)));
  }
}
