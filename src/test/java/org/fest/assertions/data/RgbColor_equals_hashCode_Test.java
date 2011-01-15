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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertFalse;
import static org.fest.assertions.data.RgbColor.color;
import static org.fest.test.EqualsHashCodeContractAssert.*;

import org.junit.*;

/**
 * Tests for <code>{@link RgbColor#equals(Object)}</code> and <code>{@link RgbColor#hashCode()}</code>.
 *
 * @author Alex Ruiz
 */
public class RgbColor_equals_hashCode_Test {

  private static RgbColor color;

  @BeforeClass public static void setUpOnce() {
    color = color(0xFFFFFF);
  }

  @Test public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(color);
  }

  @Test public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(color, color(0xFFFFFF));
  }

  @Test public void should_have_transitive_equals() {
    assertEqualsIsTransitive(color, color(0xFFFFFF), color(0xFFFFFF));
  }

  @Test public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(color, color(0xFFFFFF));
  }

  @Test public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(color.equals("WHITE"));
  }

  @Test public void should_not_be_equal_to_null() {
    assertFalse(color.equals(null));
  }

  @Test public void should_not_be_equal_to_RgbColor_with_different_red_component() {
    assertFalse(color.equals(color(0x00FFFF)));
  }

  @Test public void should_not_be_equal_to_RgbColor_with_different_green_component() {
    assertFalse(color.equals(color(0xFF00FF)));
  }

  @Test public void should_not_be_equal_to_RgbColor_with_different_blue_component() {
    assertFalse(color.equals(color(0xFFFF00)));
  }
}
