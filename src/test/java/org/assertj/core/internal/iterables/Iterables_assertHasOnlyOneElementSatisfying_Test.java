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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertHasOnlyOneElementSatisfying(AssertionInfo, Iterable, Consumer)}</code>.
 *
 * @author Vladimir Chernikov
 */
class Iterables_assertHasOnlyOneElementSatisfying_Test extends IterablesBaseTest {

  private List<Jedi> actual;
  private Consumer<Jedi> consumer;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    actual = newArrayList(new Jedi("Joda", "Green"));
    consumer = jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green");
  }

  @Test
  void should_pass_if_only_one_element_satisfies_condition() {
    iterables.assertHasOnlyOneElementSatisfying(info, actual, consumer);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertHasOnlyOneElementSatisfying(info, actual, null));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(
                                                               () -> iterables.assertHasOnlyOneElementSatisfying(info, null,
                                                                                                                 consumer))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_has_only_one_element() {
    actual.add(new Jedi("Luke", "Blue"));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(
                                                               () -> iterables.assertHasOnlyOneElementSatisfying(info, actual,
                                                                                                                 null))
                                                   .withMessage(shouldHaveSize(actual, actual.size(), 1).create());
  }
}
