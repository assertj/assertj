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
 * Copyright 2012-2013 the original author or authors.
 */
package org.fest.assertions.api;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static org.fest.assertions.error.ShouldBeEmpty.shouldBeEmpty;
import static org.fest.assertions.error.ShouldContainValues.shouldContainValues;
import static org.fest.assertions.error.TableShouldContainColumns.tableShouldContainColumns;
import static org.fest.assertions.error.TableShouldContainRows.tableShouldContainRows;

/**
 * @author Jan Gorman
 */
public final class TableAssert<R, C, V> extends AbstractAssert<TableAssert<R, C, V>, Table<R, C, V>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected TableAssert(Table<R, C, V> actual) {
    super(actual, TableAssert.class);
  }

  /**
   * Verifies that the actual {@link Table} contains the given rows.
   * 
   * @param rows The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * 
   * @throws IllegalArgumentException if no param rows have been set.
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} does not contain the given rows.
   */
  public TableAssert<R, C, V> containsRows(R... rows) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(rows != null, "The rows to look for should not be null.");
    checkArgument(rows.length > 0, "The rows to look for should not be empty.");

    Set<R> rowsNotFound = Sets.newHashSet();
    for (R row : rows) {
      if (!actual.containsRow(row)) {
        rowsNotFound.add(row);
      }
    }

    if (!rowsNotFound.isEmpty()) {
      throw failures.failure(info, tableShouldContainRows(actual, rows, rowsNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given columns.
   * 
   * @param columns The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * 
   * @throws IllegalArgumentException if no param columns have been set.
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} does not contain the given columns.
   */
  public TableAssert<R, C, V> containsColumns(C... columns) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(columns != null, "The columns to look for should not be null.");
    checkArgument(columns.length > 0, "The columns to look for should not be empty.");

    Set<C> columnsNotFound = Sets.newHashSet();
    for (C column : columns) {
      if (!actual.containsColumn(column)) {
        columnsNotFound.add(column);
      }
    }

    if (!columnsNotFound.isEmpty()) {
      throw failures.failure(info, tableShouldContainColumns(actual, columns, columnsNotFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given values for any key.
   * 
   * @param values The values to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * 
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} does not contain the given values.
   */
  public TableAssert<R, C, V> containsValues(V... values) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(values != null, "The values to look for should not be null.");
    checkArgument(values.length > 0, "The values to look for should not be empty.");

    Set<V> valuesNotFound = Sets.newHashSet();
    for (V value : values) {
      if (!actual.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }

    if (!valuesNotFound.isEmpty()) {
      throw failures.failure(info, shouldContainValues(actual, values, valuesNotFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link Table} is empty.
   * 
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} is not empty.
   */
  public void isEmpty() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEmpty()) {
      throw failures.failure(info, shouldBeEmpty(actual));
    }
  }
}
