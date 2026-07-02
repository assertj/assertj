/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.guava.api;

import static com.google.common.base.Preconditions.checkArgument;
import static org.assertj.guava.error.ShouldContainValues.shouldContainValues;
import static org.assertj.guava.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.guava.error.TableShouldContainCell.tableShouldContainCell;
import static org.assertj.guava.error.TableShouldContainColumns.tableShouldContainColumns;
import static org.assertj.guava.error.TableShouldContainRows.tableShouldContainRows;
import static org.assertj.guava.error.TableShouldHaveColumnCount.tableShouldHaveColumnCount;
import static org.assertj.guava.error.TableShouldHaveRowCount.tableShouldHaveRowCount;

import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.error.ShouldBeEmpty;
import org.assertj.core.error.ShouldNotBeEmpty;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/// @author Jan Gorman
public class TableAssert<R, C, V> extends AbstractAssert<TableAssert<R, C, V>, Table<R, C, V>> {

  protected TableAssert(Table<R, C, V> actual) {
    super(actual, TableAssert.class);
  }

  /// Verifies that the actual [Table] has the expected number of rows.
  ///
  /// Example :
  ///
  /// ```java
  /// Table <Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).hasRowCount(2);
  /// ```
  ///
  /// @param expectedSize the expected number of rows in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if the expected size is negative
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not have the expected row size.
  public TableAssert<R, C, V> hasRowCount(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.rowKeySet().size() != expectedSize) {
      throw assertionError(tableShouldHaveRowCount(actual, actual.rowKeySet().size(), expectedSize));
    }
    return myself;
  }

  /// Verifies that the actual [Table] has the expected number of columns.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).hasColumnCount(3);
  /// ```
  ///
  /// @param expectedSize the expected number of columns in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if the expected size is negative
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not have the expected column size.
  public TableAssert<R, C, V> hasColumnCount(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.columnKeySet().size() != expectedSize) {
      throw assertionError(tableShouldHaveColumnCount(actual, actual.columnKeySet().size(), expectedSize));
    }
    return myself;
  }

  /// Verifies that the actual [Table] has the expected number of cells.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).hasSize(3);
  /// ```
  ///
  /// @param expectedSize the expected number of cells in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if the expected size is negative
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not have the expected number of cells.
  public TableAssert<R, C, V> hasSize(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.size() != expectedSize) {
      throw assertionError(shouldHaveSize(actual, actual.size(), expectedSize));
    }
    return myself;
  }

  /// Verifies that the actual [Table] contains the given rows.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).containsRows(1, 2);
  /// ```
  ///
  /// @param rows The rows to look for in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if no param rows have been set.
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not contain the given rows.
  public TableAssert<R, C, V> containsRows(@SuppressWarnings("unchecked") R... rows) {
    isNotNull();
    checkArgument(rows != null, "The rows to look for should not be null.");
    checkArgument(rows.length > 0, "The rows to look for should not be empty.");

    Set<R> rowsNotFound = Sets.newHashSet();
    for (R row : rows) {
      if (!actual.containsRow(row)) {
        rowsNotFound.add(row);
      }
    }

    if (!rowsNotFound.isEmpty()) {
      throw assertionError(tableShouldContainRows(actual, rows, rowsNotFound));
    }
    return myself;
  }

  /// Verifies that the actual [Table] contains the given columns.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).containsColumns(3, 4);
  /// ```
  ///
  /// @param columns The columns to look for in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if no param columns have been set.
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not contain the given columns.
  public TableAssert<R, C, V> containsColumns(@SuppressWarnings("unchecked") C... columns) {
    isNotNull();
    checkArgument(columns != null, "The columns to look for should not be null.");
    checkArgument(columns.length > 0, "The columns to look for should not be empty.");

    Set<C> columnsNotFound = Sets.newHashSet();
    for (C column : columns) {
      if (!actual.containsColumn(column)) {
        columnsNotFound.add(column);
      }
    }

    if (!columnsNotFound.isEmpty()) {
      throw assertionError(tableShouldContainColumns(actual, columns, columnsNotFound));
    }

    return myself;
  }

  /// Verifies that the actual [Table] contains the given values for any key.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).containsValues("Franklin Pierce", "Millard Fillmore");
  /// ```
  ///
  /// @param values The values to look for in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws IllegalArgumentException if no param values have been set.
  /// @throws AssertionError           if the actual [Table] is `null`.
  /// @throws AssertionError           if the actual [Table] does not contain the given values.
  public TableAssert<R, C, V> containsValues(@SuppressWarnings("unchecked") V... values) {
    isNotNull();
    checkArgument(values != null, "The values to look for should not be null.");
    checkArgument(values.length > 0, "The values to look for should not be empty.");

    Set<V> valuesNotFound = Sets.newHashSet();
    for (V value : values) {
      if (!actual.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }

    if (!valuesNotFound.isEmpty()) {
      throw assertionError(shouldContainValues(actual, values, valuesNotFound));
    }

    return myself;
  }

  /// Verifies that the actual [Table] contains the mapping of row/column to value.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  /// actual.put(1, 4, "Franklin Pierce");
  /// actual.put(2, 5, "Grover Cleveland");
  ///
  /// assertThat(actual).containsCell(1, 3, "Millard Fillmore");
  /// ```
  ///
  /// @param row The row key to lookup in the actual [Table]
  /// @param column The column key to lookup in the actual [Table]
  /// @param expectedValue The value to look for in the actual [Table]
  /// @return this [TableAssert] for assertion chaining.
  /// @throws AssertionError if the actual [Table] is `null`.
  /// @throws AssertionError if the row key is `null`.
  /// @throws AssertionError if the column key is `null`.
  /// @throws AssertionError if the expected value is `null`.
  public TableAssert<R, C, V> containsCell(R row, C column, V expectedValue) {
    isNotNull();
    checkArgument(row != null, "The row to look for should not be null.");
    checkArgument(column != null, "The column to look for should not be null.");
    checkArgument(expectedValue != null, "The value to look for should not be null.");

    V actualValue = actual.get(row, column);
    if (!expectedValue.equals(actualValue)) {
      throw assertionError(tableShouldContainCell(actual, row, column, expectedValue, actualValue));
    }

    return myself;
  }

  /// Verifies that the actual [Table] is empty.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// assertThat(actual).isEmpty();
  /// ```
  ///
  /// @throws AssertionError if the actual [Table] is `null`.
  /// @throws AssertionError if the actual [Table] is not empty.
  public void isEmpty() {
    isNotNull();
    if (!actual.isEmpty()) {
      throw assertionError(ShouldBeEmpty.shouldBeEmpty(actual));
    }
  }

  /// Verifies that the actual [Table] is not empty.
  ///
  /// Example :
  ///
  /// ```java
  /// Table<Integer, Integer, String> actual = HashBasedTable.create();
  ///
  /// actual.put(1, 3, "Millard Fillmore");
  ///
  /// assertThat(actual).isNotEmpty();
  /// ```
  ///
  /// @return this [TableAssert] for assertion chaining.
  /// @throws AssertionError if the actual [Table] is `null`.
  /// @throws AssertionError if the actual [Table] is empty.
  ///
  /// @since 3.27.0
  public TableAssert<R, C, V> isNotEmpty() {
    isNotNull();
    if (actual.isEmpty()) {
      throw assertionError(ShouldNotBeEmpty.shouldNotBeEmpty());
    }
    return myself;
  }

  private void checkExpectedSizeArgument(int expectedSize) {
    checkArgument(expectedSize >= 0, "The expected size should not be negative.");
  }
}
