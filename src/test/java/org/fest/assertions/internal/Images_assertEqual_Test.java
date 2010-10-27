/*
 * Created on Oct 21, 2010
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
package org.fest.assertions.internal;

import static java.awt.Color.BLUE;
import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.internal.Images.*;
import static org.fest.assertions.test.TestData.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;

import org.fest.assertions.core.*;
import org.fest.assertions.test.TestData;
import org.junit.*;

/**
 * Tests for <code>{@link Images#assertEqual(AssertionInfo, BufferedImage, BufferedImage)}</code>.
 *
 * @author Yvonne Wang
 */
public class Images_assertEqual_Test {

  private static WritableAssertionInfo info;
  private static BufferedImage actual;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = fivePixelBlueImage();
  }

  private Failures failures;
  private Images images;

  @Before public void setUp() {
    failures = spy(Failures.instance());
    images = new Images();
    images.failures = failures;
  }

  @Test public void should_fail_if_images_have_different_size() {
    BufferedImage expected = newImage(6, 6, BLUE);
    try {
      images.assertEqual(info, actual, expected);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotHaveSize(actual, sizeOf(actual), sizeOf(expected)));
  }

  @Test public void should_fail_if_images_have_same_size_but_different_color() {
    BufferedImage expected = fivePixelYellowImage();
    try {
      images.assertEqual(info, actual, expected);
      fail();
    } catch (AssertionError e) {
      assertEquals("expected:<color[r=255,g=255,b=0]> but was:<color[r=0,g=0,b=255]> at pixel [0,0]", e.getMessage());
    }
    verify(failures).failure(info, doesNotHaveEqualColor(TestData.BLUE, TestData.YELLOW, 0, 0));
  }
}
