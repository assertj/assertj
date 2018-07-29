/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

/**
 * Creates an error message indicating that an assertion - that verifies that size of a value is
 * less than or equal to a certain size - failed.
 *
 * @author Martin Tarjányi
 */
public class ShouldHaveSizeLessThanOrEqualTo extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSizeLessThanOrEqualTo}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param boundary the given size to compare the actual size to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSizeLessThanOrEqualTo(Object actual, int actualSize, int boundary) {
    return new ShouldHaveSizeLessThanOrEqualTo(actual, actualSize, boundary);
  }

  private ShouldHaveSizeLessThanOrEqualTo(Object actual, int actualSize, int boundary) {
    super(format("%nExpected size to be less than or equal to:<%s> but was:<%s> in:%n<%s>", boundary, actualSize, "%s"), actual);
  }
}
