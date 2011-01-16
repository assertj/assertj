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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.data.RgbColor.color;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.util.Objects.areEqual;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.*;
import org.fest.assertions.error.*;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link BufferedImage}</code>s.
 *
 * @author Yvonne Wang
 */
public class Images {

  private static final Images INSTANCE = new Images();
  private static final Offset<Integer> ZERO = offset(0);

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Images instance() {
    return INSTANCE;
  }

  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting Images() {}

  /**
   * Asserts that two images are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, BufferedImage actual, BufferedImage expected) {
    // BufferedImage does not have an implementation of 'equals,' which means that "equality" is verified by identity.
    // We need to verify that two images are equal ourselves.
    assertEqualSize(info, actual, sizeOf(actual), sizeOf(expected));
    assertEqualColor(info, actual, expected, ZERO);
  }

  private void assertEqualSize(AssertionInfo info, BufferedImage actual, Dimension actualSize, Dimension expectedSize) {
    if (areEqual(actualSize, expectedSize)) return;
    throw failures.failure(info, doesNotHaveSize(actual, actualSize, expectedSize));
  }

  private void assertEqualColor(AssertionInfo info, BufferedImage actual, BufferedImage expected, Offset<Integer> offset) {
    int w = actual.getWidth();
    int h = actual.getHeight();
    for (int x = 0; x < w; x++)
      for (int y = 0; y < h; y++)
        assertEqual(info, color(actual.getRGB(x, y)), color(expected.getRGB(x, y)), y, x, offset);
  }

  private void assertEqual(AssertionInfo info, RgbColor actual, RgbColor expected, int y, int x, Offset<Integer> offset) {
    if (actual.isEqualTo(expected, offset)) return;
    throw failures.failure(info, doesNotHaveEqualColor(actual, expected, x, y));
  }

  @VisibleForTesting static ErrorMessage doesNotHaveEqualColor(RgbColor actual, RgbColor expected, int x, int y) {
    return new BasicErrorMessage("expected:<%s> but was:<%s> at pixel [%s,%s]", expected, actual, x, y);
  }

  @VisibleForTesting static Dimension sizeOf(BufferedImage image) {
    return new Dimension(image.getWidth(), image.getHeight());
  }
}
