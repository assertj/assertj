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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Predicate;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_allMatch_Test extends AtomicReferenceArrayAssertBaseTest {

  private Predicate<Object> predicate;

  @BeforeEach
  void beforeOnce() {
    predicate = o -> o != null;
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.allMatch(predicate);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(info(), newArrayList(internalArray()), predicate, PredicateDescription.GIVEN);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    AtomicReferenceArray<String> emptyArray = atomicArrayOf();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(emptyArray).allMatch(e -> e.contains("error")));
    // THEN
    then(assertionError).hasMessageContaining("Expecting actual not to be empty");
  }
}
