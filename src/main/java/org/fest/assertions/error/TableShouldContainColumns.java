package org.fest.assertions.error;

import java.util.Set;

/**
 * @author Jan Gorman
 */
public class TableShouldContainColumns extends BasicErrorMessageFactory {

  public static ErrorMessageFactory tableShouldContainColumns(Object actual, Object[] rows, Set<?> columnsNotFound) {
    return rows.length == 1 ? new TableShouldContainColumns(actual, rows[0]) : new TableShouldContainRows(actual, rows,
        columnsNotFound);

  }

  private TableShouldContainColumns(Object actual, Object row) {
    super("expecting:\n<%s>\n to contain column:\n<%s>", actual, row);
  }

  public TableShouldContainColumns(Object actual, Object[] rows, Set<?> columnsNotFound) {
    super("expecting:\n<%s>\n to contain columns:\n<%s>\n but could not find:\n<%s>", actual, rows, columnsNotFound);
  }
}
