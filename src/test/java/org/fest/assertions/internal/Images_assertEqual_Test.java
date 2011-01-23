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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.awt.Color.BLUE;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.internal.ColorComparisonResult.notEqual;
import static org.fest.assertions.internal.Images.doesNotHaveEqualColor;
import static org.fest.assertions.test.TestData.*;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.junit.*;

/**
 * Tests for <code>{@link Images#assertEqual(AssertionInfo, BufferedImage, BufferedImage)}</code>.
 *
 * @author Yvonne Wang
 */
public class Images_assertEqual_Test {

  private static BufferedImage actual;

  @BeforeClass public static void setUpOnce() {
    actual = fivePixelBlueImage();
  }

  private Failures failures;
  private Images images;

  @Before public void setUp() {
    failures = spy(new Failures());
    images = new Images();
    images.failures = failures;
  }

  @Test public void should_pass_if_images_are_equal() {
    images.assertEqual(someInfo(), actual, newImage(5, 5, BLUE));
  }

  @Test public void should_pass_if_images_are_same() {
    images.assertEqual(someInfo(), actual, actual);
  }

  @Test public void should_fail_if_images_have_different_size() {
    AssertionInfo info = someInfo();
    BufferedImage expected = newImage(6, 6, BLUE);
    try {
      images.assertEqual(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, doesNotHaveSize(actual, sizeOf(actual), sizeOf(expected)));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  private static Dimension sizeOf(BufferedImage image) {
    return new Dimension(image.getWidth(), image.getHeight());
  }

  @Test public void should_fail_if_images_have_same_size_but_different_color() {
    AssertionInfo info = someInfo();
    BufferedImage expected = fivePixelYellowImage();
    try {
      images.assertEqual(info, actual, expected);
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      verify(failures).failure(info, doesNotHaveEqualColor(notEqual(blue(), yellow(), 0, 0)));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
