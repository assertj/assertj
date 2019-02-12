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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Iterator;

import org.assertj.core.api.AbstractIteratorAssert;
import org.assertj.core.api.IteratorAssert;
import org.assertj.core.api.IteratorAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIteratorAssert#isExhausted()} ()}</code>.
 *
 * @author Stephan Windmüller
 */
public class IteratorAssert_isExhausted_Test extends IteratorAssertBaseTest {

  @Override
  protected IteratorAssert<Object> invoke_api_method() {
    return assertions.isExhausted();
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterators).assertIsExhausted(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void should_pass_for_exhausted_iterator() {
    Iterator<Object> iterator = emptyList().iterator();
    assertThat(iterator).isExhausted();
  }

  @Test
  public void should_fail_if_actual_has_at_least_one_element() {
    // GIVEN
    Iterator<Integer> iterator = newArrayList(1).iterator();
    // WHEN
    AssertionError error = catchThrowableOfType(assertThat(iterator)::isExhausted, AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining(shouldBeExhausted().create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator<Object> iterator = null;
    // WHEN
    AssertionError error = catchThrowableOfType(assertThat(iterator)::isExhausted, AssertionError.class);
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

}
