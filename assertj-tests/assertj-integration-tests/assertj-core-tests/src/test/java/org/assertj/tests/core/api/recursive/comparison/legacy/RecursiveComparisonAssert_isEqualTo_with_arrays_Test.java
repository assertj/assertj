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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison.legacy;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.Author;
import org.assertj.tests.core.api.recursive.data.WithObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_with_arrays_Test extends WithLegacyIntrospectionStrategyBaseTest {

  @ParameterizedTest(name = "author 1 {0} / author 2 {1}")
  @MethodSource
  void should_pass_when_comparing_same_array_fields(Author[] authors1, Author[] authors2) {
    // GIVEN
    WithArray<Author> actual = new WithArray<>(authors1);
    WithArray<Author> expected = new WithArray<>(authors2);
    // THEN
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  static Stream<Arguments> should_pass_when_comparing_same_array_fields() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Author[] empty = array();
    return Stream.of(Arguments.of(array(pratchett), array(pratchett)),
                     Arguments.of(array(pratchett, georgeMartin), array(pratchett, georgeMartin)),
                     Arguments.of(array(pratchett, none), array(pratchett, none)),
                     Arguments.of(empty, empty));
  }

  @ParameterizedTest(name = "authors 1 {0} / authors 2 {1} / difference {2}")
  @MethodSource
  void should_fail_when_comparing_different_array_fields(Author[] authors1, Author[] authors2, ComparisonDifference difference) {
    // GIVEN
    WithArray<Author> actual = new WithArray<>(authors1);
    WithArray<Author> expected = new WithArray<>(authors2);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  static Stream<Arguments> should_fail_when_comparing_different_array_fields() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    return Stream.of(Arguments.of(array(pratchett), array(georgeMartin),
                                  javaTypeDiff("group.[0].name", "Terry Pratchett", "George Martin")),
                     Arguments.of(array(pratchett, georgeMartin), array(pratchett),
                                  diff("group", array(pratchett, georgeMartin), array(pratchett),
                                       "actual and expected values are arrays of different size, actual size=2 when expected size=1")),
                     Arguments.of(array(pratchett), array(none), diff("group.[0]", pratchett, null, null)),
                     Arguments.of(array(none), array(pratchett), diff("group.[0]", null, pratchett, null)));
  }

  @ParameterizedTest(name = "authors {0} / object {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource
  void should_fail_when_comparing_array_to_non_array(Object actualFieldValue, Author[] expectedFieldValue,
                                                     String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithObject actual = new WithObject(actualFieldValue);
    WithArray<Author> expected = new WithArray<>(expectedFieldValue);
    // WHEN/THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  static Stream<Arguments> should_fail_when_comparing_array_to_non_array() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    // we need to use the actual array and the expected list otherwise compareRecursivelyFailsWithDifferences
    // fails as actualArray and expectedList description includes their instance reference (@123ff3f) to differentiate their
    // otherwise similar description
    Author[] expectedFieldValue = array(pratchett, georgeMartin);
    List<Author> actualFieldValue = list(pratchett, georgeMartin);
    return Stream.of(Arguments.of(pratchett, array(pratchett), "group", pratchett, array(pratchett),
                                  "expected field is an array but actual field is not (org.assertj.tests.core.api.recursive.data.Author)"),
                     Arguments.of(actualFieldValue, expectedFieldValue, "group", actualFieldValue, expectedFieldValue,
                                  "expected field is an array but actual field is not (java.util.ArrayList)"));
  }

  public static class WithArray<E> {
    public E[] group;

    @SafeVarargs
    public WithArray(E... items) {
      this.group = items;
    }

    @Override
    public String toString() {
      return "WithArray group=%s".formatted(list(group));
    }

  }

}
