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
package org.assertj.tests.core.api;

import static org.assertj.core.api.BDDAssertions.thenDate;

import java.util.Date;

import org.assertj.core.api.AbstractDateAssert;
import org.junit.jupiter.api.Test;

class BDDAssertions_thenDate_Test {

  @Test
  void should_accept_Date() {
    // GIVEN
    Date actual = new Date(0);
    // WHEN
    AbstractDateAssert<?> result = thenDate(actual);
    // THEN
    result.isEqualTo(new Date(0));
  }

  // https://github.com/assertj/assertj/issues/839
  @Test
  void should_accept_bounded_generic_Date_subtype() {
    // WHEN
    AbstractDateAssert<?> result = thenDate(getDate());
    // THEN
    result.isNotNull();
  }

  @SuppressWarnings("unchecked")
  private static <T extends Date> T getDate() {
    return (T) new Date(0);
  }

}
