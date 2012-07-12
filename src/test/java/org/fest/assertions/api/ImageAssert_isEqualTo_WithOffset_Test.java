/*
 * Created on Jan 24, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.test.TestData.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;

import org.fest.assertions.data.Offset;
import org.fest.assertions.internal.Images;
import org.junit.*;

/**
 * Tests for <code>{@link ImageAssert#isEqualTo(BufferedImage, Offset)}</code>.
 * 
 * @author Yvonne Wang
 */
public class ImageAssert_isEqualTo_WithOffset_Test {

  private static Offset<Integer> offset;

  @BeforeClass
  public static void setUpOnce() {
    offset = offset(6);
  }

  private Images images;
  private ImageAssert assertions;

  @Before
  public void setUp() {
    images = mock(Images.class);
    assertions = new ImageAssert(fivePixelBlueImage());
    assertions.images = images;
  }

  @Test
  public void should_verify_that_actual_is_equal_to_expected() {
    BufferedImage expected = fivePixelYellowImage();
    assertions.isEqualTo(expected, offset);
    verify(images).assertEqual(assertions.info, assertions.actual, expected, offset);
  }

  @Test
  public void should_return_this() {
    ImageAssert returned = assertions.isEqualTo(fivePixelBlueImage(), offset);
    assertSame(assertions, returned);
  }
}
