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
package org.assertj.core.api.iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Iterator;

import org.assertj.core.api.AbstractIteratorAssert;
import org.assertj.core.api.IteratorAssert;
import org.assertj.core.api.IteratorAssertBaseTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIteratorAssert#isExhausted()} ()}</code>.
 *
 * @author Stephan Windmüller
 */
public class IteratorAssert_isExhausted_Test extends IteratorAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

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
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(newArrayList(1).iterator()).isExhausted();
    // WHEN
    AssertionError error = catchThrowableOfType(softly::assertAll, AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining("\nExpecting iterator to be exhausted.");
  }

}
