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

import org.junit.jupiter.api.BeforeEach;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

/**
 * @author Jan Gorman
 */
public class TableAssertBaseTest {

  protected Table<Integer, Integer, String> actual;

  @BeforeEach
  public void setUp() {
    actual = TreeBasedTable.create();
    actual.put(1, 3, "Millard Fillmore");
    actual.put(1, 4, "Franklin Pierce");
    actual.put(2, 5, "Grover Cleveland");
  }

}
