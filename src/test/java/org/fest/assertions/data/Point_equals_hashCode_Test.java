/*
 * Created on Jan 22, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertFalse;
import static org.fest.assertions.data.Point.atPoint;
import static org.fest.test.EqualsHashCodeContractAssert.*;

import org.junit.*;

/**
 * Tests for <code>{@link Index#equals(Object)}</code> and <code>{@link Index#hashCode()}</code>.
 *
 * @author Yvonne Wang
 */
public class Point_equals_hashCode_Test {

  private static Point point;

  @BeforeClass public static void setUpOnce() {
    point = atPoint(6, 8);
  }

  @Test public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(point);
  }

  @Test public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(point, atPoint(6, 8));
  }

  @Test public void should_have_transitive_equals() {
    assertEqualsIsTransitive(point, atPoint(6, 8), atPoint(6, 8));
  }

  @Test public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(point, atPoint(6, 8));
  }

  @Test public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(point.equals("6, 8"));
  }

  @Test public void should_not_be_equal_to_null() {
    assertFalse(point.equals(null));
  }

  @Test public void should_not_be_equal_to_Index_with_different_x() {
    assertFalse(point.equals(atPoint(8, 8)));
  }

  @Test public void should_not_be_equal_to_Index_with_different_y() {
    assertFalse(point.equals(atPoint(6, 6)));
  }
}
