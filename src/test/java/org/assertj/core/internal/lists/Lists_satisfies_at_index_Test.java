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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Lists#satisfies(AssertionInfo, List, Consumer, Index)}</code>.
 *
 * @author Jacek Jackowiak
 */
public class Lists_satisfies_at_index_Test extends ListsBaseTest {

  private final AssertionInfo info = someInfo();
  private final Consumer<String> shouldBeLuke = str -> assertThat(str).isEqualTo("Luke");
  private final Index index = atIndex(1);
  private final List<String> jedis = newArrayList("Leia", "Luke", "Yoda");

  @Test
  public void should_pass_if_element_at_index_matches_the_requirements() {
    lists.satisfies(info, jedis, shouldBeLuke, index);
  }

  @Test
  public void should_fail_if_element_at_index_does_not_match_the_requirements() {
    thrown.expectAssertionError("expected:<\"[Luke]\"> but was:<\"[Yoda]\">");
    lists.satisfies(info, jedis, shouldBeLuke, atIndex(2));
  }

  @Test
  public void should_fail_if_index_is_out_of_bound() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive) but was:%n <3>");
    lists.satisfies(info, jedis, shouldBeLuke, atIndex(3));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(shouldNotBeNull());
    lists.satisfies(info, null, shouldBeLuke, index);
  }

  @Test
  public void should_fail_if_requirements_are_null() {
    thrown.expectNullPointerException("The Consumer expressing the assertions requirements must not be null");
    lists.satisfies(info, jedis, null, index);
  }

  @Test
  public void should_fail_if_index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.satisfies(info, jedis, shouldBeLuke, null);
  }
}
