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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#containsSubsequence(List)}</code>.
 *
 * @author Chris Arnott
 */
public class IterableAssert_containsSubsequence_List_Test extends IterableAssertBaseTest {

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    // IterableAssertBaseTest is testing Iterable<Object>, so the List type needs to be Object
    // or the {@link AbstractIterableAssert#containsSequence(Object...)} method is called.
    return assertions.containsSubsequence(newArrayList((Object) "Luke", "Leia"));
  }

  @Override
  protected void verify_internal_effects() {
	  verify(iterables).assertContainsSubsequence(getInfo(assertions), getActual(assertions), array("Luke", "Leia"));
  }

  @Test
  public void should_throw_error_if_subsequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      List<Object> nullList = null;
      assertions.containsSubsequence(nullList);
    }).withMessage(nullSubsequence());
  }
}
