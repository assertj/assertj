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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatComparable;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.test.Name;
import org.junit.jupiter.api.Test;

class Assertions_assertThatComparable_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    Name comparable = new Name("abc");
    // WHEN
    GenericComparableAssertV2<Name> assertions = assertThatComparable(comparable);
    // THEN
    then(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    Name comparable = new Name("abc");
    // WHEN
    GenericComparableAssertV2<Name> assertions = assertThatComparable(comparable);
    // THEN
    then(assertions.actual).isSameAs(comparable);
  }

  @Test
  void all_comparable_assertions_should_work_with_non_generic_comparable() {
    // GIVEN
    Name name1 = new Name("abc");
    Name name2 = new Name("abc");
    Name name3 = new Name("bcd");
    Name name4 = new Name("cde");
    // WHEN/THEN
    assertThatComparable(name3).isBetween(name1, name4);
    assertThatComparable(name3).isStrictlyBetween(name1, name4);
    assertThatComparable(name1).isEqualByComparingTo(name2);
    assertThatComparable(name1).isNotEqualByComparingTo(name3);
    assertThatComparable(name1).isEqualByComparingTo(name2);
    assertThatComparable(name1).isLessThan(name3);
    assertThatComparable(name1).isLessThanOrEqualTo(name3);
    assertThatComparable(name3).isGreaterThan(name1);
    assertThatComparable(name3).isGreaterThanOrEqualTo(name1);
  }

  @Test
  void all_comparable_assertions_should_work_with_generic_comparable() {
    // GIVEN
    Comparable<Name> name1 = new Name("abc");
    Comparable<Name> name2 = new Name("abc");
    Comparable<Name> name3 = new Name("bcd");
    Comparable<Name> name4 = new Name("cde");
    // WHEN/THEN
    assertThatComparable(name1).isLessThan(name3);
    assertThatComparable(name1).isEqualByComparingTo(name2);
    assertThatComparable(name3).isBetween(name1, name4);
    assertThatComparable(name3).isStrictlyBetween(name1, name4);
    assertThatComparable(name1).isNotEqualByComparingTo(name3);
    assertThatComparable(name1).isEqualByComparingTo(name2);
    assertThatComparable(name1).isLessThanOrEqualTo(name3);
    assertThatComparable(name3).isGreaterThan(name1);
    assertThatComparable(name3).isGreaterThanOrEqualTo(name1);
  }

}
