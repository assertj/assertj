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

import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/**
 * @author Jan Gorman
 */
public class TableAssert<R, C, V> extends AbstractAssert<TableAssert<R, C, V>, Table<R, C, V>> {

  protected TableAssert(Table<R, C, V> actual) {
    super(actual, TableAssert.class);
  }

  /**
   * Verifies that the actual {@link Table} has the expected number of rows.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table &lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).hasRowCount(2);</code></pre>
   *
   * @param expectedSize The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if the expected size is negative
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not have the expected row size.
   */
  public TableAssert<R, C, V> hasRowCount(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.rowKeySet().size() != expectedSize) {
      throw assertionError(tableShouldHaveRowCount(actual, actual.rowKeySet().size(), expectedSize));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} has the expected number of columns.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).hasColumnCount(3);</code></pre>
   *
   * @param expectedSize The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if the expected size is negative
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not have the expected column size.
   */
  public TableAssert<R, C, V> hasColumnCount(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.columnKeySet().size() != expectedSize) {
      throw assertionError(tableShouldHaveColumnCount(actual, actual.columnKeySet().size(), expectedSize));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} has the expected number of cells.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).hasSize(3);</code></pre>
   *
   * @param expectedSize The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if the expected size is negative
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not have the expected number of cells.
   */
  public TableAssert<R, C, V> hasSize(int expectedSize) {
    isNotNull();
    checkExpectedSizeArgument(expectedSize);

    if (actual.size() != expectedSize) {
      throw assertionError(shouldHaveSize(actual, actual.size(), expectedSize));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given rows.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsRows(1, 2);</code></pre>
   *
   * @param rows The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param rows have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given rows.
   */
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

  /**
   * Verifies that the actual {@link Table} contains the given columns.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsColumns(3, 4);</code></pre>
   *
   * @param columns The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param columns have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given columns.
   */
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

  /**
   * Verifies that the actual {@link Table} contains the given values for any key.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsValues("Franklin Pierce", "Millard Fillmore");</code></pre>
   *
   * @param values The values to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given values.
   */
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

  /**
   * Verifies that the actual {@link Table} contains the mapping of row/column to value.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsCell(1, 3, "Millard Fillmore");</code></pre>
   *
   * @param row The row key to lookup in the actual {@link Table}
   * @param column The column key to lookup in the actual {@link Table}
   * @param expectedValue The value to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the row key is {@code null}.
   * @throws AssertionError if the column key is {@code null}.
   * @throws AssertionError if the expected value is {@code null}.
   */
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

  /**
   * Verifies that the actual {@link Table} is empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Table&lt;Integer, Integer, String&gt; actual = HashBasedTable.create();
   *
   * assertThat(actual).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} is not empty.
   */
  public void isEmpty() {
    isNotNull();
    if (!actual.isEmpty()) {
      throw assertionError(ShouldBeEmpty.shouldBeEmpty(actual));
    }
  }

  private void checkExpectedSizeArgument(int expectedSize) {
    checkArgument(expectedSize >= 0, "The expected size should not be negative.");
  }
}
