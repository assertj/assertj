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

import java.util.Set;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * @author Jan Gorman
 */
public class TableShouldContainRows extends BasicErrorMessageFactory {

  public static ErrorMessageFactory tableShouldContainRows(Object actual, Object[] rows, Set<?> rowsNotFound) {
    return rows.length == 1 ? new TableShouldContainRows(actual, rows[0])
        : new TableShouldContainRows(actual, rows,
                                     rowsNotFound);

  }

  private TableShouldContainRows(Object actual, Object row) {
    super("%nExpecting:%n  %s%nto contain row:%n  %s", actual, row);
  }

  public TableShouldContainRows(Object actual, Object[] rows, Set<?> rowsNotFound) {
    super("%nExpecting:%n  %s%nto contain rows:%n  %s%nbut could not find:%n  %s", actual, rows, rowsNotFound);
  }
}
