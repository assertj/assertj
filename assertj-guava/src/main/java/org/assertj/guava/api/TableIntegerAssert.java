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

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.AbstractIntegerAssert;

/**
 * Assertion methods for {@link Integer}s with the possibility of returning to the {@link TableAssert}.
 *
 * @param <R> the type of row keys in the source table
 * @param <C> the type of column keys in the source table
 * @param <V> the type of values in the source table
 */
public class TableIntegerAssert<R, C, V> extends AbstractIntegerAssert<TableIntegerAssert<R, C, V>> {

  private final TableAssert<R, C, V> source;

  /**
   * Creates a new integer assertion linked to the source table assertion.
   *
   * @param source the source table assertion
   * @param i the actual integer value
   */
  public TableIntegerAssert(TableAssert<R, C, V> source, Integer i) {
    super(i, TableIntegerAssert.class);
    this.source = source;
  }

  /**
   * Returns to the source table assertion.
   *
   * @return the source table assertion
   */
  @CheckReturnValue
  public TableAssert<R, C, V> returnToTable() {
    return source;
  }

}
