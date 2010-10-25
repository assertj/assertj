/*
 * Created on Oct 24, 2010
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

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.test.ExpectedException.none;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link RgbColor#RgbColor(int, int, int)}</code>.
 *
 * @author Yvonne Wang
 */
public class RgbColor_constructor_with_individual_RGB_values_Test {

  @Rule public ExpectedException thrown = none();

  @Test public void should_throw_error_if_R_is_less_than_zero() {
    thrown.expectIllegalArgumentException("R should be between 0 and 255 but was -1");
    new RgbColor(-1, 0, 0);
  }

  @Test public void should_throw_error_if_R_is_greater_than_255() {
    thrown.expectIllegalArgumentException("R should be between 0 and 255 but was 256");
    new RgbColor(256, 0, 0);
  }

  @Test public void should_throw_error_if_G_is_less_than_zero() {
    thrown.expectIllegalArgumentException("G should be between 0 and 255 but was -1");
    new RgbColor(0, -1, 0);
  }

  @Test public void should_throw_error_if_G_is_greater_than_255() {
    thrown.expectIllegalArgumentException("G should be between 0 and 255 but was 256");
    new RgbColor(0, 256, 0);
  }

  @Test public void should_throw_error_if_B_is_less_than_zero() {
    thrown.expectIllegalArgumentException("B should be between 0 and 255 but was -1");
    new RgbColor(0, 0, -1);
  }

  @Test public void should_throw_error_if_B_is_greater_than_255() {
    thrown.expectIllegalArgumentException("B should be between 0 and 255 but was 256");
    new RgbColor(0, 0, 256);
  }

  @Test public void should_create_RGBColor() {
    RgbColor color = new RgbColor(1, 2, 3);
    assertEquals(1, color.r);
    assertEquals(2, color.g);
    assertEquals(3, color.b);
  }
}
