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
package org.fest.assertions.internal;

import java.awt.image.BufferedImage;

import org.fest.assertions.core.AssertionInfo;

/**
 * Reusable assertions for <code>{@link BufferedImage}</code>s.
 *
 * @author Yvonne Wang
 */
public class Images {

  private static final Images INSTANCE = new Images();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Images instance() {
    return INSTANCE;
  }

  private Images() {}

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
    // TODO Auto-generated method stub
    // BufferedImage does not have an implementation of 'equals,' which means that "equality" is verified by identity.
    // We need to verify that two images are equal ourselves.
  }
}
