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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_with_arrays_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "author 1 {0} / author 2 {1}")
  @MethodSource("sameArrays")
  public void should_pass_when_comparing_same_array_fields(Author[] authors1, Author[] authors2) {
    // GIVEN
    WithArray<Author> actual = new WithArray<>(authors1);
    WithArray<Author> expected = new WithArray<>(authors2);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  static Stream<Arguments> sameArrays() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Author[] empty = array();
    return Stream.of(Arguments.of(array(pratchett), array(pratchett)),
                     Arguments.of(array(pratchett, georgeMartin), array(pratchett, georgeMartin)),
                     Arguments.of(array(pratchett, none), array(pratchett, none)),
                     Arguments.of(empty, empty));
  }

  @ParameterizedTest(name = "authors 1 {0} / authors 2 {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("differentArrays")
  public void should_fail_when_comparing_different_array_fields(Author[] authors1, Author[] authors2,
                                                                String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithArray<Author> actual = new WithArray<>(authors1);
    WithArray<Author> expected = new WithArray<>(authors2);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> differentArrays() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    return Stream.of(Arguments.of(array(pratchett), array(georgeMartin), "group.name", "Terry Pratchett", "George Martin", null),
                     Arguments.of(array(pratchett, georgeMartin), array(pratchett), "group",
                                  array(pratchett, georgeMartin), array(pratchett),
                                  "actual and expected values are arrays of different size, actual size=2 when expected size=1"),
                     Arguments.of(array(pratchett), array(none), "group", pratchett, null, null),
                     Arguments.of(array(none), array(pratchett), "group", null, pratchett, null));
  }

  @ParameterizedTest(name = "authors {0} / object {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("arrayWithNonArrays")
  public void should_fail_when_comparing_array_to_non_array(Object actualFieldValue, Author[] expectedFieldValue,
                                                            String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithObject actual = new WithObject(actualFieldValue);
    WithArray<Author> expected = new WithArray<>(expectedFieldValue);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> arrayWithNonArrays() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    // we need to use the actual array and the expected list otherwise verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall
    // fails as actualArray and expectedList description includes their instance reference (@123ff3f) to differentiate their
    // otherwise similar description
    Author[] expectedFieldValue = array(pratchett, georgeMartin);
    List<Author> actualFieldValue = list(pratchett, georgeMartin);
    return Stream.of(Arguments.of(pratchett, array(pratchett), "group", pratchett, array(pratchett),
                                  "expected field is an array but actual field is not (org.assertj.core.api.recursive.comparison.Author)"),
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
      return format("WithArray group=%s", list(group));
    }

  }

}
