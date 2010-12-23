/*
 * Created on Oct 25, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.*;
import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.data.RgbColor.color;
import static org.fest.assertions.test.ErrorMessages.offsetIsNull;
import static org.fest.assertions.test.ExpectedException.none;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link RgbColor#isEqualTo(RgbColor, Offset)}</code>.
 *
 * @author Yvonne Wang
 */
public class RgbColor_isEqualTo_Test {

  @Rule public ExpectedException thrown = none();

  private static RgbColor color;

  @BeforeClass public static void setUpOnce() {
    color = color(0x69FC62);
  }

  @Test public void should_return_true_if_both_RGBColor_are_exactly_equal() {
    assertTrue(color.isEqualTo(color(0x69FC62), offset(1)));
  }

  @Test public void should_return_true_if_difference_in_components_is_less_or_equal_than_offset() {
    assertTrue(color.isEqualTo(color(0x68FB61), offset(1)));
  }

  @Test public void should_return_false_if_difference_in_red_is_greater_than_offset() {
    assertFalse(color.isEqualTo(color(0x67FC62), offset(1)));
  }

  @Test public void should_return_false_if_difference_in_green_greater_than_offset() {
    assertFalse(color.isEqualTo(color(0x69FA62), offset(1)));
  }

  @Test public void should_return_false_if_difference_in_blue_is_greater_than_offset() {
    assertFalse(color.isEqualTo(color(0x69FC60), offset(1)));
  }

  @Test public void should_return_false_if_other_RGBColor_is_null() {
    assertFalse(color.isEqualTo(null, offset(1)));
  }

  @Test public void should_throw_error_if_offset_is_null() {
    thrown.expectNullPointerException(offsetIsNull());
    color.isEqualTo(color, null);
  }
}
