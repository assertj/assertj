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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;


public class ListAssert_size_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_be_able_to_use_integer_assertions_on_size_of_list() {
    List<Integer> ints = Arrays.asList(1, 2, 3);
    assertThat(ints).size().isGreaterThan(0).isLessThanOrEqualTo(3).returnToList().contains(1).doesNotContain(9);
  }

  @Test
  public void should_have_nice_error_message_when_size_is_used_on_list_which_is_null() {
    List<Integer> nullList = null;
    thrown.expectNullPointerException("Can not assert on size of empty list.");
    assertThat(nullList).size().isGreaterThan(1);
  }
}
