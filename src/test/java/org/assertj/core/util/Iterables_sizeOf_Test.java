/*
 * Created on Apr 29, 2007
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2007-2012 the original author or authors.
 */
package org.assertj.core.util;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.util.Lists.newArrayList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#sizeOf(Iterable)}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 */
public class Iterables_sizeOf_Test {
  @Test
  public void should_return_zero_if_iterable_is_empty() {
    assertEquals(0, Iterables.sizeOf(new ArrayList<String>()));
  }

  @Test(expected = NullPointerException.class)
  public void should_throws_exception_if_iterable_is_null() {
    Iterables.sizeOf(null);
  }

  @Test
  public void should_return_iterable_size() {
    List<String> list = newArrayList("Frodo", "Sam");
    assertEquals(2, Iterables.sizeOf(list));
  }

  @Test
  public void should_return_correct_size_for_non_collection_iterable() {
    Iterable<Throwable> sqlException = new SQLException(new Exception(new Exception()));
    assertEquals(3, Iterables.sizeOf(sqlException));
  }
}
