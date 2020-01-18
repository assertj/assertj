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
 * Creates an error message indicating that an assertion that verifies a minimum size failed.
 *
 * @author Sandra Parsick
 * @author Georg Berky
 */
public class ShouldHaveSizeGreaterThan extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSizeGreaterThan}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedMinSize the expected size.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSizeGreaterThan(Object actual, int actualSize, int expectedMinSize) {
    return new ShouldHaveSizeGreaterThan(actual, actualSize, expectedMinSize);
  }

  private ShouldHaveSizeGreaterThan(Object actual, int actualSize, int expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    // %%s is going to be formatted to %s to be replaced by actual later on.
    super(format("%n" +
                 "Expecting size of:%n" +
                 "  <%%s>%n" +
                 "to be greater than %s but was %s", expectedSize, actualSize),
          actual);
  }
}
