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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.Year;
import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.tests.core.testkit.Name;
import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_Comparable_Test {

  @Test
  void should_create_Assert_with_custom_Comparable() {
    // GIVEN
    Name actual = new Name();
    // WHEN
    AbstractComparableAssert<?, Name> assertions = assertThat(actual);
    // THEN
    then(assertions).isNotNull();
  }

  // https://github.com/assertj/assertj/issues/3491
  @Test
  void should_create_Assert_with_Year() {
    // GIVEN
    Year actual = Year.now();
    // WHEN
    AbstractComparableAssert<?, Year> result = assertThat(actual);
    // THEN
    then(result).isNotNull();
  }

  @Test
  void all_comparable_assertions_should_work_with_non_generic_comparable() {
    // GIVEN
    Name name1 = new Name("abc");
    Name name2 = new Name("abc");
    Name name3 = new Name("bcd");
    Name name4 = new Name("cde");
    // WHEN/THEN
    assertThat(name3).isBetween(name1, name4);
    assertThat(name3).isStrictlyBetween(name1, name4);
    assertThat(name1).isEqualByComparingTo(name2);
    assertThat(name1).isNotEqualByComparingTo(name3);
    assertThat(name1).isEqualByComparingTo(name2);
    assertThat(name1).isLessThan(name3);
    assertThat(name1).isLessThanOrEqualTo(name3);
    assertThat(name3).isGreaterThan(name1);
    assertThat(name3).isGreaterThanOrEqualTo(name1);
    // does not compile but assertThat(name).isGreaterThanOrEqualTo(name); does
    // Comparable<Name> name = new Name("abc");
    // assertThat(name).isGreaterThanOrEqualTo(name);
  }

  @Test
  void all_comparable_assertions_should_work_with_object_comparable() {
    // GIVEN
    Comparable<Object> name1 = new ComparingWithObject();
    Comparable<Object> name2 = new ComparingWithObject();
    Comparable<Object> name3 = new ComparingWithObject();
    Comparable<Object> name4 = new ComparingWithObject();
    // WHEN/THEN
    assertThat(name3).isBetween(name1, name4);
    // assertThat(name3).isStrictlyBetween(name1, name4);
    assertThat(name1).isEqualByComparingTo(name2);
    // assertThat(name1).isNotEqualByComparingTo(name3);
    assertThat(name1).isEqualByComparingTo(name2);
    // assertThat(name1).isLessThan(name3);
    assertThat(name1).isLessThanOrEqualTo(name3);
    // assertThat(name3).isGreaterThan(name1);
    assertThat(name3).isGreaterThanOrEqualTo(name1);
  }

  @Test
  void all_comparable_assertions_should_work_with_object_comparable_subclass() {
    // GIVEN
    ComparingWithObject name1 = new ComparingWithObject();
    ComparingWithObject name2 = new ComparingWithObject();
    ComparingWithObject name3 = new ComparingWithObject();
    ComparingWithObject name4 = new ComparingWithObject();
    // WHEN/THEN
    assertThat(name3).isBetween(name1, name4);
    // assertThat(name3).isStrictlyBetween(name1, name4);
    assertThat(name1).isEqualByComparingTo(name2);
    // assertThat(name1).isNotEqualByComparingTo(name3);
    assertThat(name1).isEqualByComparingTo(name2);
    // assertThat(name1).isLessThan(name3);
    assertThat(name1).isLessThanOrEqualTo(name3);
    // assertThat(name3).isGreaterThan(name1);
    assertThat(name3).isGreaterThanOrEqualTo(name1);
  }

  private static class ComparingWithObject implements Comparable<Object> {

    @Override
    public int compareTo(Object other) {
      return 0;
    }

  }

}
