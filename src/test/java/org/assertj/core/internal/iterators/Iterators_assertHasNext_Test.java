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
package org.assertj.core.internal.iterators;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IteratorsBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.ShouldHaveNext.shouldHaveNext;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import org.assertj.core.internal.Iterators;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

/**
 * Tests for <code>{@link Iterators#assertHasNext(AssertionInfo, Iterator)}</code>.
 *
 * @author Natália Struharová
 */
public class Iterators_assertHasNext_Test extends IteratorsBaseTest {

  @Test
  void should_pass_if_iterator_has_next() {
    // GIVEN
    List<String> list = List.of("Luke", "Yoda", "Vader");
    Iterator iterator = list.iterator();
    // WHEN
    iterator.next();
    // THEN
    iterators.assertHasNext(info, iterator);

  }

  @Test
  void should_fail_if_iterator_has_no_next() {
    // GIVEN
    List<String> list = List.of("Luke");
    Iterator iterator = list.iterator();

    // WHEN
    iterator.next();
    AssertionError error = expectAssertionError(() -> iterators.assertHasNext(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldHaveNext().create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator iterator = null;

    // WHEN
    AssertionError error = expectAssertionError(() -> iterators.assertHasNext(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }

}
