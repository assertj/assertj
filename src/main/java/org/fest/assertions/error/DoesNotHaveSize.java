/*
 * Created on Oct 18, 2010
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
package org.fest.assertions.error;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Creates an error message indicating that an assertion that verifies that two values that represent size are equal
 * failed.
 *
 * @author Alex Ruiz
 */
public class DoesNotHaveSize extends BasicErrorMessage {

  /**
   * Creates a new </code>{@link DoesNotHaveSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedSize the expected size.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage doesNotHaveSize(BufferedImage actual, Dimension actualSize, Dimension expectedSize) {
    return new DoesNotHaveSize("%sexpected image size:<%s> but was:<%s>", expectedSize, actualSize);
  }

  /**
   * Creates a new </code>{@link DoesNotHaveSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expectedSize the expected size.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage doesNotHaveSize(Collection<?> actual, int expectedSize) {
    return new DoesNotHaveSize("%sexpected size:<%s> but was:<%s> in:<%s>", expectedSize, actual.size(), actual);
  }

  private DoesNotHaveSize(String format, Object... arguments) {
    super(format, arguments);
  }
}
