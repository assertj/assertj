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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.util.Lists.newArrayList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link IterableUtil#sizeOf(Iterable)}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 */
public class IterableUtil_sizeOf_Test {
  @Test
  public void should_return_zero_if_iterable_is_empty() {
    assertThat(IterableUtil.sizeOf(new ArrayList<String>())).isEqualTo(0);
  }

  @Test(expected = NullPointerException.class)
  public void should_throws_exception_if_iterable_is_null() {
    IterableUtil.sizeOf(null);
  }

  @Test
  public void should_return_iterable_size() {
    List<String> list = newArrayList("Frodo", "Sam");
    assertThat(IterableUtil.sizeOf(list)).isEqualTo(2);
  }

  @Test
  public void should_return_correct_size_for_non_collection_iterable() {
    Iterable<Throwable> sqlException = new SQLException(new Exception(new Exception()));
    assertThat(IterableUtil.sizeOf(sqlException)).isEqualTo(3);
  }
}
