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
public class TableShouldHaveRowCount extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link TableShouldHaveRowCount}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the number of row keys in {@code actual}.
   * @param expectedSize the expected number of row keys.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory tableShouldHaveRowCount(Object actual, int actualSize, int expectedSize) {
    return new TableShouldHaveRowCount(actual, actualSize, expectedSize);
  }

  private TableShouldHaveRowCount(Object actual, int actualSize, int expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%nExpected row count: %s but was: %s in:%n%s", expectedSize, actualSize, "%s"), actual);
  }

}
