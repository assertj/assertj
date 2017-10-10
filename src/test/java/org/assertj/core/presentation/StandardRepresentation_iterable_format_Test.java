/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StandardRepresentation_iterable_format_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  public void should_return_null_if_iterable_is_null() {
    assertThat(STANDARD_REPRESENTATION.smartFormat(null)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_iterable_is_empty() {
    assertThat(STANDARD_REPRESENTATION.smartFormat(asList())).isEqualTo("[]");
  }

  @Test
  public void should_format_iterable_on_one_line_if_description_is_short_enough() {
    String e1 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription() / 10);
    String e2 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription() / 10);
    assertThat(STANDARD_REPRESENTATION.smartFormat(asList(e1, e2))).isEqualTo("[\"" + e1 + "\", \"" + e2 + "\"]");
  }

  @Test
  public void should_format_iterable_with_one_element_per_line_when_single_line_description_is_too_long() {
    String e1 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription());
    String e2 = stringOfLength(StandardRepresentation.getMaxLengthForSingleLineDescription());
    assertThat(STANDARD_REPRESENTATION.smartFormat(asList(e1, e2))).isEqualTo(format("[\"" + e1 + "\",%n" +
                                                                                     "    \"" + e2 + "\"]"));
  }

  @Test
  public void should_format_iterable_with_custom_start_and_end() {
    List<? extends Object> list = asList("First", 3);
    assertThat(STANDARD_REPRESENTATION.singleLineFormat(list, "{", "}")).isEqualTo("{\"First\", 3}");
    assertThat(STANDARD_REPRESENTATION.singleLineFormat(asList(), "{", "}")).isEqualTo("{}");
  }

  @Test
  public void should_format_iterable_with_one_element_per_line() {
    String formatted = STANDARD_REPRESENTATION.multiLineFormat(asList("First", 3, "foo", "bar"));
    String formattedAfterNewLine = org.assertj.core.util.Compatibility.System.lineSeparator() + "  <" + formatted + ">";
    assertThat(formattedAfterNewLine).isEqualTo(format("%n" +
                                                       "  <[\"First\",%n" +
                                                       "    3,%n" +
                                                       "    \"foo\",%n" +
                                                       "    \"bar\"]>"));
  }

  @Test
  public void should_format_iterable_up_to_the_maximum_allowed_elements_multi_line() {
    StandardRepresentation.setMaxElementsForPrinting(3);
    StandardRepresentation.setMaxLengthForSingleLineDescription(10);
    String formatted = STANDARD_REPRESENTATION.smartFormat(asList("First", 3, "foo", "bar"));
    String formattedAfterNewLine = org.assertj.core.util.Compatibility.System.lineSeparator() + "  <" + formatted + ">";
    assertThat(formattedAfterNewLine).isEqualTo(format("%n" +
                                                       "  <[\"First\",%n" +
                                                       "    3,%n" +
                                                       "    \"foo\",%n" +
                                                       "    ...]>"));
  }

  @Test
  public void should_format_iterable_up_to_the_maximum_allowed_elements_single_line() {
    StandardRepresentation.setMaxElementsForPrinting(3);
    String formatted = STANDARD_REPRESENTATION.smartFormat(asList("First", 3, "foo", "bar"));
    assertThat(formatted).isEqualTo("[\"First\", 3, \"foo\", ...]");
  }

  @Test
  public void should_format_iterable_with_an_element_per_line_according_the_given_representation() {
    String formatted = new HexadecimalRepresentation().multiLineFormat(asList(1, 2, 3));
    String formattedAfterNewLine = org.assertj.core.util.Compatibility.System.lineSeparator() + "  <" + formatted + ">";
    assertThat(formattedAfterNewLine).isEqualTo(format("%n" +
                                                       "  <[0x0000_0001,%n" +
                                                       "    0x0000_0002,%n" +
                                                       "    0x0000_0003]>"));
  }

  @Test
  public void should_format_recursive_iterable() {
    List<Object> list = new ArrayList<>();
    list.add(list);
    list.add(list);
    String formatted = STANDARD_REPRESENTATION.multiLineFormat(list);
    assertThat(formatted).isEqualTo(format("[(this Collection),%n" +
                                           "    (this Collection)]"));
  }

  private static String stringOfLength(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append(i % 10);
    }
    return sb.toString();
  }

}
