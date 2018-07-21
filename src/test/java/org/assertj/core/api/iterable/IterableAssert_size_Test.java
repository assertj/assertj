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
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class IterableAssert_size_Test {

  @Test
  public void should_be_able_to_use_integer_assertions_on_iterable_size() {
    Iterable<String> strings = new HashSet<>(asList("a", "b", "c"));
    // @format:off
    assertThat(strings).size().isGreaterThan(0)
                              .isLessThanOrEqualTo(3)
                       .returnToIterable().contains("a")
                                          .doesNotContain("d");
    Iterable<Integer> integers = asList(1, 2, 3);
    assertThat(integers).size().isGreaterThan(0)
                               .isLessThanOrEqualTo(3)
                        .returnToIterable().contains(1)
                                           .doesNotContain(4);
    // @format:on
  }

  @Test
  public void should_have_an_helpful_error_message_when_size_is_used_on_a_null_iterable() {
    Iterable<Integer> nullList = null;
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullList).size().isGreaterThan(1))
                                    .withMessage("Can not perform assertions on the size of a null iterable.");
  }
}
