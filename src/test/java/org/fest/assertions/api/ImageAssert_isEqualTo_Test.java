/*
 * Created on Oct 20, 2010
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;

import org.fest.assertions.internal.Images;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ImageAssert#isEqualTo(BufferedImage)}</code>.
 *
 * @author Yvonne Wang
 */
public class ImageAssert_isEqualTo_Test {

  private Images images;
  private ImageAssert assertions;

  @Before public void setUp() {
    images = mock(Images.class);
    assertions = new ImageAssert(fivePixelBlueImage());
    assertions.images = images;
  }

  @Test public void should_verify_that_actual_is_equal_to_expected() {
    BufferedImage expected = fivePixelYellowImage();
    assertions.isEqualTo(expected);
    verify(images).assertEqual(assertions.info, assertions.actual, expected);
  }

  @Test public void should_return_this() {
    ImageAssert returned = assertions.isEqualTo(fivePixelBlueImage());
    assertSame(assertions, returned);
  }

}
