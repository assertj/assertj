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
 * Creates an error message indicating that an assertion that verifies that a value have certain size failed.
 * 
 * @author Nicolas François
 */
public class ShouldHaveSameSizeAs extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSameSizeAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedSize the expected size.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameSizeAs(Object actual, Object actualSize, Object expectedSize) {
    return new ShouldHaveSameSizeAs(actual, actualSize, expectedSize);
  }

  private ShouldHaveSameSizeAs(Object actual, Object actualSize, Object expectedSize) {
     // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
     // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%nActual and expected should have same size but actual size is:%n <%s>%n" +
        "while expected is:%n <%s>%nActual was:%n<%s>", actualSize, expectedSize, "%s"), actual);
  }
}
