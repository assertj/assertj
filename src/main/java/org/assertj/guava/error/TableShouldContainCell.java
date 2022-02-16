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

import com.google.common.collect.Table;

/**
 * @author David Harris
 */
public class TableShouldContainCell extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link TableShouldContainCell}</code>.
   * @param actual the actual value in the failed assertion.
   * @param row the row where actualValue was read.
   * @param column the column where actualValue was read.
   * @param expectedValue the expected value of the cell.
   * @param actualValue the expected actual value of the cell.
   *
  * @param <R> the type of the table row keys
  * @param <C> the type of the table column keys
  * @param <V> the type of the mapped values
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <R, C, V> ErrorMessageFactory tableShouldContainCell(Table<R, C, V> actual, R row, C column, V expectedValue,
                                                                     V actualValue) {
    return new TableShouldContainCell(actual, row, column, expectedValue, actualValue);
  }

  private <R, C, V> TableShouldContainCell(Table<R, C, V> actual, R row, C column, V expectedValue, V actualValue) {
    // Except for actual, format values using the standard representation instead of a specific one like Hexadecimal
    super(format("%nExpecting row: %s and column: %s to have value:%n  %s%nbut was:%n  %s%nin:%n  %s", row, column,
                 expectedValue, actualValue, "%s"),
          actual);
  }

}
