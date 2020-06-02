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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StandardRepresentation_iterable_format_Test extends AbstractBaseRepresentationTest {

  @Test
  public void should_return_null_if_iterable_is_null() {
    // GIVEN
    List<Object> list = null;
    // WHEN
    String formatted = STANDARD_REPRESENTATION.smartFormat(list);
    // THEN
    then(formatted).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_iterable_is_empty() {
    // GIVEN
    List<Object> list = emptyList();
    // WHEN
    String formatted = STANDARD_REPRESENTATION.smartFormat(list);
    // THEN
    then(formatted).isEqualTo("[]");
  }

  @Test
  public void should_format_iterable_on_one_line_if_description_is_short_enough() {
    // GIVEN
    String element1 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription() / 10);
    String element2 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription() / 10);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.smartFormat(list(element1, element2));
    // THEN
    then(formatted).isEqualTo("[\"" + element1 + "\", \"" + element2 + "\"]");
  }

  @Test
  public void should_format_iterable_with_one_element_per_line_when_single_line_description_is_too_long() {
    String element1 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription());
    String element2 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription());
    // WHEN
    String formatted = STANDARD_REPRESENTATION.smartFormat(list(element1, element2));
    // THEN
    then(formatted).isEqualTo(format("[\"" + element1 + "\",%n" +
                                     "    \"" + element2 + "\"]"));
  }

  @Test
  public void should_format_iterable_with_custom_start_and_end() {
    // GIVEN
    List<? extends Object> list = list("First", 3);
    // THEN
    then(STANDARD_REPRESENTATION.singleLineFormat(list, "{", "}")).isEqualTo("{\"First\", 3}");
    then(STANDARD_REPRESENTATION.singleLineFormat(list(), "{", "}")).isEqualTo("{}");
  }

  @ParameterizedTest(name = "with printing {0} max, {1} should be formatted as {2}")
  @MethodSource("should_format_iterable_source")
  public void should_format_iterable(int maxElementsForPrinting, List<?> list, String expectedDescription) {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(maxElementsForPrinting);
    StandardRepresentation.setMaxLengthForSingleLineDescription(15);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.toStringOf(list);
    // THEN
    // formattedAfterNewLine is built to show we align values on the first element.
    String formattedAfterNewLine = "  <" + formatted + ">";
    then(formattedAfterNewLine).isEqualTo(format(expectedDescription));
  }

  private static Stream<Arguments> should_format_iterable_source() {
    return Stream.of(Arguments.of(12, list(1, 2, 3, 4, 5), "  <[1, 2, 3, 4, 5]>"),
                     Arguments.of(12, list("First", 3, "foo", "bar"), "  <[\"First\",%n" +
                                                                      "    3,%n" +
                                                                      "    \"foo\",%n" +
                                                                      "    \"bar\"]>"),
                     Arguments.of(12, list("First", 3, 4, "foo", "bar", 5, "another", 6), "  <[\"First\",%n" +
                                                                                          "    3,%n" +
                                                                                          "    4,%n" +
                                                                                          "    \"foo\",%n" +
                                                                                          "    \"bar\",%n" +
                                                                                          "    5,%n" +
                                                                                          "    \"another\",%n" +
                                                                                          "    6]>"),
                     Arguments.of(12, list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), "  <[1,%n" +
                                                                           "    2,%n" +
                                                                           "    3,%n" +
                                                                           "    4,%n" +
                                                                           "    5,%n" +
                                                                           "    6,%n" +
                                                                           "    7,%n" +
                                                                           "    8,%n" +
                                                                           "    9,%n" +
                                                                           "    10]>"),
                     Arguments.of(12, list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), "  <[1,%n" +
                                                                                   "    2,%n" +
                                                                                   "    3,%n" +
                                                                                   "    4,%n" +
                                                                                   "    5,%n" +
                                                                                   "    6,%n" +
                                                                                   "    7,%n" +
                                                                                   "    8,%n" +
                                                                                   "    9,%n" +
                                                                                   "    10,%n" +
                                                                                   "    11,%n" +
                                                                                   "    12]>"),
                     Arguments.of(11, list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19), "  <[1,%n" +
                                                                                                               "    2,%n" +
                                                                                                               "    3,%n" +
                                                                                                               "    4,%n" +
                                                                                                               "    5,%n" +
                                                                                                               "    6,%n" +
                                                                                                               "    ...%n" +
                                                                                                               "    15,%n" +
                                                                                                               "    16,%n" +
                                                                                                               "    17,%n" +
                                                                                                               "    18,%n" +
                                                                                                               "    19]>"),
                     Arguments.of(12, list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20), "  <[1,%n" +
                                                                                                                   "    2,%n" +
                                                                                                                   "    3,%n" +
                                                                                                                   "    4,%n" +
                                                                                                                   "    5,%n" +
                                                                                                                   "    6,%n" +
                                                                                                                   "    ...%n" +
                                                                                                                   "    15,%n" +
                                                                                                                   "    16,%n" +
                                                                                                                   "    17,%n" +
                                                                                                                   "    18,%n" +
                                                                                                                   "    19,%n" +
                                                                                                                   "    20]>"));
  }

  @Test
  public void should_format_iterable_up_to_the_maximum_allowed_elements_single_line() {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(6);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.toStringOf(list("First", 3, 4, "foo", "bar", 5, "another", 6));
    // THEN
    then(formatted).isEqualTo(format("[\"First\", 3, 4, ... 5, \"another\", 6]"));
  }

  @Test
  public void should_format_iterable_with_an_element_per_line_according_the_given_representation() {
    // GIVEN
    List<Integer> list = list(1, 2, 3);
    // WHEN
    String formatted = new HexadecimalRepresentation().toStringOf(list);
    // THEN
    then(formatted).isEqualTo("[0x0000_0001, 0x0000_0002, 0x0000_0003]");
  }

  @Test
  public void should_format_recursive_iterable() {
    // GIVEN
    List<Object> selfReferencingList = list();
    selfReferencingList.add(selfReferencingList);
    selfReferencingList.add(selfReferencingList);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.toStringOf(selfReferencingList);
    // THEN
    then(formatted).isEqualTo(format("[(this iterable), (this iterable)]"));
  }

  @Test
  public void should_format_iterable_having_itself_as_element() {
    // GIVEN
    List<Object> selfReferencingList = list("Hello");
    selfReferencingList.add(selfReferencingList);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.toStringOf(selfReferencingList);
    // THEN
    then(formatted).isEqualTo("[\"Hello\", (this iterable)]");
  }

  @Test
  public void should_only_consider_root_object_for_cycles() {
    List<Object> innerList = list(1, 2, 3);
    List<Object> outerList = list(innerList, innerList);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.toStringOf(outerList);
    // THEN
    then(formatted).isEqualTo("[[1, 2, 3], [1, 2, 3]]");
  }

  private static String stringOfLength(int length) {
    return Stream.generate(() -> "a").limit(length).collect(joining());
  }

}
