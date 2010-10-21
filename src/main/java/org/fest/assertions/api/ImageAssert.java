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

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for images. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(BufferedImage)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Ansgar Konermann
 */
public class ImageAssert implements Assert<BufferedImage> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Images images = Images.instance();

  @VisibleForTesting final BufferedImage actual;
  @VisibleForTesting final WritableAssertionInfo info;


  protected ImageAssert(BufferedImage actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public ImageAssert as(String description) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert as(Description description) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public ImageAssert describedAs(Description description) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert isEqualTo(BufferedImage expected) {
    images.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public ImageAssert isNotEqualTo(BufferedImage other) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public void isNull() {
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public ImageAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ImageAssert isSameAs(BufferedImage expected) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert isNotSameAs(BufferedImage other) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert satisfies(Condition<BufferedImage> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert doesNotSatisfy(Condition<BufferedImage> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert is(Condition<BufferedImage> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public ImageAssert isNot(Condition<BufferedImage> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  String descriptionText() {
    return info.descriptionText();
  }
}
