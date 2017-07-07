/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

/**
 * Creates an error message indicating that an assertion that verifies that a value have certain number of lines failed.
 * 
 * @author Mariusz Smykula
 */
public class ShouldHaveLineCount extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveLineCount}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the lines count of {@code actual}.
   * @param expectedSize the expected lines count.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveLinesCount(Object actual, int actualSize, int expectedSize) {
    return new ShouldHaveLineCount(actual, actualSize, expectedSize);
  }

  private ShouldHaveLineCount(Object actual, int actualSize, int expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    // Also don't indent actual first line since the remaining lines won't have any indentation
    super(format("%nExpecting text:%n%s%nto have <%s> lines but had <%s>.", "%s", expectedSize, actualSize), actual);
  }
}
