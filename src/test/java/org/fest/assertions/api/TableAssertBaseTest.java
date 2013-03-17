package org.fest.assertions.api;

import org.junit.Before;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Jan Gorman
 */
public class TableAssertBaseTest extends BaseTest {

  protected Table<Integer, Integer, String> actual;

  @Before
  public void setUp() {
    actual = HashBasedTable.create();
    actual.put(1, 3, "Millard Fillmore");
    actual.put(1, 4, "Franklin Pierce");
    actual.put(2, 5, "Grover Cleveland");
  }

}
