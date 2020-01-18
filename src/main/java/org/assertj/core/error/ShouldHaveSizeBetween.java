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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

/**
 * Creates an error message indicating that an assertion - that verifies that size of a value is
 * between two given values - failed.
 *
 * @author Martin Tarjányi
 */
public class ShouldHaveSizeBetween extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSizeBetween}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSizeBetween(Object actual, int actualSize, int lowerBoundary, int higherBoundary) {
    return new ShouldHaveSizeBetween(actual, actualSize, lowerBoundary, higherBoundary);
  }

  private ShouldHaveSizeBetween(Object actual, int actualSize, int lowerBoundary, int higherBoundary) {
    super(format("%nExpected size to be between: <%s> and <%s> but was:<%s> in:%n<%s>", lowerBoundary, higherBoundary,
                 actualSize, "%s"),
          actual);
  }
}
