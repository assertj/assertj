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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.error;

import static java.lang.String.format;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * @author David Harris
 */
public class TableShouldHaveColumnCount extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link TableShouldHaveColumnCount}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the number of column keys in {@code actual}.
   * @param expectedSize the expected number of column keys.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory tableShouldHaveColumnCount(Object actual, int actualSize, int expectedSize) {
    return new TableShouldHaveColumnCount(actual, actualSize, expectedSize);
  }

  private TableShouldHaveColumnCount(Object actual, int actualSize, int expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%nExpected column count: %s but was: %s in:%n%s", expectedSize, actualSize, "%s"), actual);
  }

}
