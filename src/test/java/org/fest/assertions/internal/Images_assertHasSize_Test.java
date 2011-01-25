/*
 * Created on Jan 24, 2010
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
package org.fest.assertions.internal;

import static java.awt.Color.BLUE;
import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.internal.Images.sizeOf;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.*;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Images#assertHasSize(AssertionInfo, BufferedImage, Dimension)}</code>.
 *
 * @author Yvonne Wang
 */
public class Images_assertHasSize_Test {

  private static BufferedImage actual;

  @BeforeClass public static void setUpOnce() {
    actual = newImage(6, 8, BLUE);
  }

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Images images;

  @Before public void setUp() {
    failures = spy(new Failures());
    images = new Images();
    images.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    images.assertHasSize(someInfo(), null, new Dimension());
  }

  @Test public void should_throw_error_if_size_is_null() {
    thrown.expectNullPointerException("The given size should not be null");
    images.assertHasSize(someInfo(), actual, null);
  }

  @Test public void should_pass_if_actual_has_size() {
    images.assertHasSize(someInfo(), actual, new Dimension(6, 8));
  }

  @Test public void should_fail_if_actual_has_different_width() {
    AssertionInfo info = someInfo();
    Dimension size = new Dimension(10, 8);
    try {
      images.assertHasSize(someInfo(), actual, size);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSizesAreNotEqual(info, size);
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  @Test public void should_fail_if_actual_has_different_height() {
    AssertionInfo info = someInfo();
    Dimension size = new Dimension(6, 10);
    try {
      images.assertHasSize(someInfo(), actual, size);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSizesAreNotEqual(info, size);
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  private void verifyFailureThrownWhenSizesAreNotEqual(AssertionInfo info, Dimension size) {
    verify(failures).failure(info, shouldHaveSize(actual, sizeOf(actual), size));
  }
}
