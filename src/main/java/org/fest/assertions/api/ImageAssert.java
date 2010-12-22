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

import java.awt.image.BufferedImage;

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
 */
public class ImageAssert extends AbstractAssert<ImageAssert, BufferedImage> {

  @VisibleForTesting Images images = Images.instance();

  protected ImageAssert(BufferedImage actual) {
    super(actual, ImageAssert.class);
  }

  /** {@inheritDoc} */
  @Override public ImageAssert isEqualTo(BufferedImage expected) {
    images.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  @Override public ImageAssert isNotEqualTo(BufferedImage other) {
    // TODO Auto-generated method stub
    return null;
  }
}
