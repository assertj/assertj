/*
 * Created on Oct 20, 2010
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

import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.data.RgbColor.color;
import static org.fest.assertions.error.ShouldBeEqualColors.shouldBeEqualColors;
import static org.fest.assertions.error.ShouldBeEqualImages.shouldBeEqualImages;
import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.error.ShouldNotBeEqualImages.shouldNotBeEqualImages;
import static org.fest.assertions.internal.ColorComparisonResult.*;
import static org.fest.assertions.internal.CommonValidations.checkOffsetIsNotNull;
import static org.fest.util.Objects.areEqual;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.*;
import org.fest.assertions.error.ErrorMessageFactory;
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

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Images() {}

  /**
   * Asserts that two images are equal. Two images are equal if:
   * <ol>
   * <li>they have equal size</li>
   * <li>the the RGB values of the color at each pixel are equal</li>
   * </ol>
   * @param info contains information about the assertion.
   * @param actual the actual image.
   * @param expected the expected image.
   * @throws AssertionError if the actual image is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, BufferedImage actual, BufferedImage expected) {
    assertEqual(info, actual, expected, ZERO);
  }

  /**
   * Asserts that two images are equal. Two images are equal if:
   * <ol>
   * <li>they have the same size</li>
   * <li>the difference between the RGB values of the color at each pixel is less than or equal to the given offset</li>
   * </ol>
   * @param info contains information about the assertion.
   * @param actual the actual image.
   * @param expected the expected image.
   * @param offset helps decide if the color of two pixels are similar: two pixels that are identical to the human eye may still
   *          have slightly different color values. For example, by using an offset of 1 we can indicate that a blue value of 60
   *          is similar to a blue value of 61.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual image is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, BufferedImage actual, BufferedImage expected, Offset<Integer> offset) {
    checkOffsetIsNotNull(offset);
    if (areEqual(actual, expected)) return;
    if (actual == null || expected == null) throw imagesShouldBeEqual(info, offset);
    // BufferedImage does not have an implementation of 'equals,' which means that "equality" is verified by identity.
    // We need to verify that two images are equal ourselves.
    if (!haveEqualSize(actual, expected)) throw imageShouldHaveSize(info, actual, sizeOf(actual), sizeOf(expected));
    ColorComparisonResult haveEqualColor = haveEqualColor(actual, expected, offset);
    if (haveEqualColor == ARE_EQUAL) return;
    throw failures.failure(info, imagesShouldHaveEqualColor(haveEqualColor, offset));
  }

  private AssertionError imagesShouldBeEqual(AssertionInfo info, Offset<Integer> offset) {
    return failures.failure(info, shouldBeEqualImages(offset));
  }

  private ErrorMessageFactory imagesShouldHaveEqualColor(ColorComparisonResult r, Offset<Integer> offset) {
    return shouldBeEqualColors(r.color2, r.color1, r.point, offset);
  }

  /**
   * Asserts that two images are not equal.
   * @param info contains information about the assertion.
   * @param actual the given image.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if {@code actual} is equal to {@code other}.
   */
  public void assertNotEqual(AssertionInfo info, BufferedImage actual, BufferedImage other) {
    if (areEqual(actual, other)) throw imagesShouldNotBeEqual(info);
    if (actual == null || other == null) return;
    if (!(haveEqualSize(actual, other))) return;
    ColorComparisonResult haveEqualColor = haveEqualColor(actual, other, ZERO);
    if (haveEqualColor != ARE_EQUAL) return;
    throw imagesShouldNotBeEqual(info);
  }

  private AssertionError imagesShouldNotBeEqual(AssertionInfo info) {
    return failures.failure(info, shouldNotBeEqualImages());
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

  /**
   * Asserts that the size of the given image is equal to the given size.
   * @param info contains information about the assertion.
   * @param actual the given image.
   * @param size the expected size of {@code actual}.
   * @throws NullPointerException if the given size is {@code null}.
   * @throws AssertionError if the size of the given image is not equal to the given size.
   */
  public void assertHasSize(AssertionInfo info, BufferedImage actual, Dimension size) {
    if (size == null) throw new NullPointerException("The given size should not be null");
    Objects.instance().assertNotNull(info, actual);
    Dimension sizeOfActual = sizeOf(actual);
    if (areEqual(sizeOfActual, size)) return;
    throw imageShouldHaveSize(info, actual, sizeOfActual, size);
  }

  private AssertionError imageShouldHaveSize(AssertionInfo info, BufferedImage image, Dimension actual, Dimension expected) {
    return failures.failure(info, shouldHaveSize(image, actual, expected));
  }

  @VisibleForTesting
  static Dimension sizeOf(BufferedImage image) {
    return new Dimension(image.getWidth(), image.getHeight());
  }
}
