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
package org.fest.assertions.api;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import org.fest.assertions.data.Offset;
import org.fest.assertions.internal.Images;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for images.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(BufferedImage)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ImageAssert extends AbstractAssert<ImageAssert, BufferedImage> {

  @VisibleForTesting
  Images images = Images.instance();

  protected ImageAssert(BufferedImage actual) {
    super(actual, ImageAssert.class);
  }

  /**
   * Verifies that the actual image is equal to the given one. Two images are equal if:
   * <ol>
   * <li>they have equal size</li>
   * <li>the the RGB values of the color at each pixel are equal</li>
   * </ol>
   * @param expected the given image to compare the actual image to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual image is not equal to the given one.
   */
  @Override
  public ImageAssert isEqualTo(BufferedImage expected) {
    images.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual image is equal to the given one. Two images are equal if:
   * <ol>
   * <li>they have the same size</li>
   * <li>the difference between the RGB values of the color at each pixel is less than or equal to the given offset</li>
   * </ol>
   * @param expected the given image to compare the actual image to.
   * @param offset helps decide if the color of two pixels are similar: two pixels that are identical to the human eye may still
   *          have slightly different color values. For example, by using an offset of 1 we can indicate that a blue value of 60
   *          is similar to a blue value of 61.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual image is not equal to the given one.
   */
  public ImageAssert isEqualTo(BufferedImage expected, Offset<Integer> offset) {
    images.assertEqual(info, actual, expected, offset);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ImageAssert isNotEqualTo(BufferedImage other) {
    images.assertNotEqual(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual image has the given size.
   * @param expected the expected size of the actual image.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given size is {@code null}.
   * @throws AssertionError if the size of the actual image is not equal to the given size.
   */
  public ImageAssert hasSize(Dimension expected) {
    images.assertHasSize(info, actual, expected);
    return this;
  }

  @Override
  public ImageAssert usingComparator(Comparator<? super BufferedImage> customComparator) {
    throw new UnsupportedOperationException("custom Comparator is not supported for image comparison");
  }

  @Override
  public ImageAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.images = Images.instance();
    return myself;
  }
}
