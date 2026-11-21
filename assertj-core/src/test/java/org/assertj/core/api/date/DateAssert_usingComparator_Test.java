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
package org.assertj.core.api.date;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBeforeOrEqualTo.shouldBeBeforeOrEqualTo;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Comparator;
import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.Test;

class DateAssert_usingComparator_Test extends DateAssertBaseTest {

  private final Comparator<Date> comparator = alwaysEqual();

  @Test
  void using_default_comparator_test() {
    // WHEN
    assertions.usingDefaultComparator();
    // THEN
    then(getObjects(assertions)).isSameAs(Objects.instance());
  }

  @Test
  void using_custom_comparator_test() {
    // WHEN
    assertions.usingComparator(comparator);
    // THEN
    then(getObjects(assertions).getComparator()).isSameAs(comparator);
  }

  @Test
  void date_specific_assertions_should_honor_custom_comparator() {
    // GIVEN
    Date actual = new Date(1000);
    Date other = new Date(0);
    // WHEN/THEN
    then(actual).usingComparator(comparator).isBeforeOrEqualTo(other);
  }

  @Test
  void date_specific_assertions_should_use_default_comparator_after_usingDefaultComparator() {
    // GIVEN
    Date actual = new Date(1000);
    Date other = new Date(0);
    // WHEN
    var assertionError = expectAssertionError(() -> then(actual).usingComparator(comparator)
                                                                .usingDefaultComparator()
                                                                .isBeforeOrEqualTo(other));
    // THEN
    then(assertionError).hasMessage(shouldBeBeforeOrEqualTo(actual, other).create());
  }

}
