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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Throwables#getRootCause(Throwable)}.
 * 
 * @author Jean-Christophe Gay
 */
class Throwables_getRootCause_Test {

  @Test
  void should_return_null_if_throwable_has_no_cause() {
    assertThat(Throwables.getRootCause(new Throwable())).isNull();
  }

  @Test
  void should_return_cause_when_throwable_has_cause() {
    IllegalArgumentException expectedCause = new IllegalArgumentException();
    assertThat(Throwables.getRootCause(new Throwable(expectedCause))).isSameAs(expectedCause);
  }

  @Test
  void should_return_root_cause_when_throwable_has_cause_which_has_cause() {
    NullPointerException expectedCause = new NullPointerException();
    Throwable error = new Throwable(new IllegalArgumentException(expectedCause));

    assertThat(Throwables.getRootCause(error)).isSameAs(expectedCause);
  }

  @Test
  void should_reject_cause_cycle_including_initial_throwable() {
    // GIVEN
    Throwable first = new Throwable("first");
    Throwable second = new Throwable("second", first);
    first.initCause(second);
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> Throwables.getRootCause(first))
                                  .withMessage("Cycle detected in throwable cause chain");
  }

  @Test
  void should_reject_cause_cycle_below_initial_throwable() {
    // GIVEN
    Throwable first = new Throwable("first");
    Throwable second = new Throwable("second", first);
    first.initCause(second);
    Throwable actual = new Throwable(first);
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> Throwables.getRootCause(actual))
                                  .withMessage("Cycle detected in throwable cause chain");
  }

  @Test
  void should_distinguish_equal_but_separate_throwables() {
    // GIVEN
    Throwable rootCause = new EqualThrowable(null);
    Throwable actual = new EqualThrowable(rootCause);
    // WHEN
    Throwable result = Throwables.getRootCause(actual);
    // THEN
    then(result).isSameAs(rootCause);
  }

  private static class EqualThrowable extends Throwable {
    EqualThrowable(Throwable cause) {
      super(cause);
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof EqualThrowable;
    }

    @Override
    public int hashCode() {
      return 1;
    }
  }
}
