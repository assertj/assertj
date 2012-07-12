/*
 * Created on Oct 21, 2010
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
package org.fest.assertions.internal;

import static java.awt.Color.BLUE;
import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.data.Point.atPoint;
import static org.fest.assertions.error.ShouldBeEqualColors.shouldBeEqualColors;
import static org.fest.assertions.error.ShouldBeEqualImages.shouldBeEqualImages;
import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.internal.Images.sizeOf;
import static org.fest.assertions.test.TestData.*;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Offset;
import org.junit.*;

/**
 * Tests for <code>{@link Images#assertEqual(AssertionInfo, BufferedImage, BufferedImage)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Images_assertEqual_Test {

  private static BufferedImage actual;
  private static Offset<Integer> offset;

  @BeforeClass
  public static void setUpOnce() {
    actual = fivePixelBlueImage();
    offset = offset(0);
  }

  private Failures failures;
  private Images images;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    images = new Images();
    images.failures = failures;
  }

  @Test
  public void should_pass_if_images_are_equal() {
    images.assertEqual(someInfo(), actual, newImage(5, 5, BLUE));
  }

  @Test
  public void should_pass_if_images_are_same() {
    images.assertEqual(someInfo(), actual, actual);
  }

  @Test
  public void should_pass_if_both_images_are_null() {
    images.assertEqual(someInfo(), null, null);
  }

  @Test
  public void should_fail_if_actual_is_null_and_expected_is_not() {
    AssertionInfo info = someInfo();
    try {
      images.assertEqual(someInfo(), null, fivePixelBlueImage());
    } catch (AssertionError e) {
      verifyFailureThrownWhenImagesAreNotEqual(info);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_is_null_and_actual_is_not() {
    AssertionInfo info = someInfo();
    try {
      images.assertEqual(someInfo(), actual, null);
    } catch (AssertionError e) {
      verifyFailureThrownWhenImagesAreNotEqual(info);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenImagesAreNotEqual(AssertionInfo info) {
    verify(failures).failure(info, shouldBeEqualImages(offset));
  }

  @Test
  public void should_fail_if_images_have_different_size() {
    AssertionInfo info = someInfo();
    BufferedImage expected = newImage(6, 6, BLUE);
    try {
      images.assertEqual(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSize(actual, sizeOf(actual), sizeOf(expected)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_images_have_same_size_but_different_color() {
    AssertionInfo info = someInfo();
    BufferedImage expected = fivePixelYellowImage();
    try {
      images.assertEqual(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqualColors(yellow(), blue(), atPoint(0, 0), offset));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
