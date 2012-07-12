/*
 * Created on Jan 21, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.awt.Color.BLUE;
import static org.fest.assertions.error.ShouldNotBeEqualImages.shouldNotBeEqualImages;
import static org.fest.assertions.test.TestData.*;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.junit.*;

/**
 * Tests for <code>{@link Images#assertNotEqual(AssertionInfo, BufferedImage, BufferedImage)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Images_assertNotEqual_Test {

  private static BufferedImage actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = fivePixelBlueImage();
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
  public void should_pass_if_actual_is_null_and_expected_is_not() {
    images.assertNotEqual(someInfo(), null, fivePixelBlueImage());
  }

  @Test
  public void should_pass_if_expected_is_null_and_actual_is_not() {
    images.assertNotEqual(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_images_have_different_size() {
    images.assertNotEqual(someInfo(), actual, newImage(3, 3, BLUE));
  }

  @Test
  public void should_pass_if_images_have_different_color() {
    images.assertNotEqual(someInfo(), actual, fivePixelYellowImage());
  }

  @Test
  public void should_fail_if_images_are_equal() {
    AssertionInfo info = someInfo();
    BufferedImage other = newImage(5, 5, BLUE);
    try {
      images.assertNotEqual(info, actual, other);
    } catch (AssertionError e) {
      verifyFailureThrownWhenImagesAreEqual(info);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_images_are_same() {
    AssertionInfo info = someInfo();
    try {
      images.assertNotEqual(info, actual, actual);
    } catch (AssertionError e) {
      verifyFailureThrownWhenImagesAreEqual(info);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenImagesAreEqual(AssertionInfo info) {
    verify(failures).failure(info, shouldNotBeEqualImages());
  }
}
