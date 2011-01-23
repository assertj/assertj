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
import static org.fest.assertions.error.IsEqual.isEqual;
import static org.fest.assertions.internal.ColorComparisonResult.*;
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
   * @param actual the actual image.
   * @param expected the expected image.
   * @throws AssertionError if the actual image is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual images are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, BufferedImage actual, BufferedImage expected) {
    if (areEqual(actual, expected)) return;
    // BufferedImage does not have an implementation of 'equals,' which means that "equality" is verified by identity.
    // We need to verify that two images are equal ourselves.
    if (!haveEqualSize(actual, expected))
      throw failures.failure(info, doesNotHaveSize(actual, sizeOf(actual), sizeOf(expected)));
    ColorComparisonResult haveEqualColor = haveEqualColor(actual, expected, ZERO);
    if (ARE_EQUAL != haveEqualColor)
      throw failures.failure(info, doesNotHaveEqualColor(haveEqualColor));
  }

  private static Dimension sizeOf(BufferedImage image) {
    return new Dimension(image.getWidth(), image.getHeight());
  }

  @VisibleForTesting static ErrorMessage doesNotHaveEqualColor(ColorComparisonResult r) {
    return new BasicErrorMessage("expected:<%s> but was:<%s> at pixel [%s,%s]", r.c2, r.c1, r.x, r.y);
  }

  /**
   * Asserts that two images are not equal.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if {@code actual} is equal to {@code other}.
   */
  public void assertNotEqual(AssertionInfo info, BufferedImage actual, BufferedImage other) {
    if (areEqual(actual, other)) throw imagesAreEqual(info, actual, other);
    if (!haveEqualSize(actual, other)) return;
    ColorComparisonResult haveEqualColor = haveEqualColor(actual, other, ZERO);
    if (haveEqualColor != ARE_EQUAL) return;
    throw imagesAreEqual(info, actual, other);
  }

  private AssertionError imagesAreEqual(AssertionInfo info, BufferedImage actual, BufferedImage other) {
    return failures.failure(info, isEqual(actual, other));
  }

  private boolean haveEqualSize(BufferedImage i1, BufferedImage i2) {
    return i1.getWidth() == i2.getWidth() && i1.getHeight() == i2.getHeight();
  }

  private ColorComparisonResult haveEqualColor(BufferedImage i1, BufferedImage i2, Offset<Integer> offset) {
    int w = i1.getWidth();
    int h = i1.getHeight();
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        RgbColor c1 = color(i1.getRGB(x, y));
        RgbColor c2 = color(i2.getRGB(x, y));
        if (c1.isEqualTo(c2, offset)) continue;
        return notEqual(c1, c2, x, y);
      }
    }
    return ARE_EQUAL;
  }
}
