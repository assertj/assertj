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
    AbstractRawComparableAssert<?, Name> assertions = assertThatComparable(comparable);
    // THEN
    then(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    Name comparable = new Name("abc");
    // WHEN
    AbstractRawComparableAssert<?, Name> assertions = assertThatComparable(comparable);
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
    // fail with a class cast exception not great as we can pass any comparable value here
    // assertThatComparable(name3).isBetween(name1, "");
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

  @Test
  void all_comparable_assertions_should_work_with_generic_jdk_comparable() {
    // GIVEN
    Comparable<String> name1 = "abc";
    Comparable<String> name2 = "abc";
    Comparable<String> name3 = "bcd";
    Comparable<String> name4 = "cde";
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

  @Test
  void all_comparable_assertions_should_work_with_non_generic_comparable_subclass() {
    // GIVEN
    CoolName name1 = new CoolName("abc");
    CoolName name2 = new CoolName("abc");
    CoolName name3 = new CoolName("bcd");
    CoolName name4 = new CoolName("cde");
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

  static class CoolName extends Name {
    String nickName;

    public CoolName(String first) {
      super(first);
    }

  }

  @Test
  void comparable_assertions_should_work_with_object_comparable() {
    // GIVEN
    Comparable<Object> name1 = new ComparingWithObject();
    Comparable<Object> name3 = new ComparingWithObject();
    Comparable<Object> name4 = new ComparingWithObject();
    // WHEN/THEN
    assertThatComparable(name3).isBetween(name1, name4);
  }

//  @Test
//  void comparable_assertions_should_work_with_wildcard_comparable() {
//    // GIVEN
//    Comparable<?> name1 = new ComparingWithObject();
//    Comparable<?> name3 = new ComparingWithObject();
//    Comparable<?> name4 = new ComparingWithObject();
//    // WHEN/THEN
//    assertThatComparable(name3).isBetween(name1, name4); // does not compile
//  }
//
//  @Test
//  void comparable_assertions_should_fail_when_comparing_uncompatible_types() {
//    // GIVEN
//    Comparable<?> name1 = new Name("abc");
//    Comparable<?> name2 = "bcd";
//    // WHEN/THEN
//    thenExceptionOfType(ClassCastException.class).isThrownBy(() -> assertThatComparable(name1).isLessThan(name2));  // does not compile
//  }

  @Test
  void comparable_assertions_should_work_with_object_comparable_subclass() {
    // GIVEN
    ComparingWithObject o1 = new ComparingWithObject();
    ComparingWithObject o2 = new ComparingWithObject();
    ComparingWithObject o3 = new ComparingWithObject();
    // WHEN/THEN
    assertThatComparable(o1).isBetween(o2, o3);
  }

  static class ComparingWithObject implements Comparable<Object> {
    @Override
    public int compareTo(Object other) {
      return 0;
    }
  }

}
