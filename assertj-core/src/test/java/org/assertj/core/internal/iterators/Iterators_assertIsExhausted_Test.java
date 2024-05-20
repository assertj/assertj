/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.iterators;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.Iterator;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterators;
import org.assertj.core.internal.IteratorsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterators#assertIsExhausted(AssertionInfo, Iterator)}</code>.
 *
 * @author Natália Struharová
 */
class Iterators_assertIsExhausted_Test extends IteratorsBaseTest {

  @Test
  void should_pass_if_iterator_is_exhausted() {
    // GIVEN
    Iterator<String> actual = list("Luke").iterator();
    actual.next();
    // WHEN/THEN
    iterators.assertIsExhausted(INFO, actual);
  }

  @Test
  void should_fail_if_iterator_is_not_exhausted() {
    // WHEN
    Iterator<String> actual = list("Luke").iterator();
    expectAssertionError(() -> iterators.assertIsExhausted(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeExhausted());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator<String> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> iterators.assertIsExhausted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }
}
