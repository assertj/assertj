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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.HexadecimalRepresentation.HEXA_REPRESENTATION;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.IterableUtil.multiLineFormat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class IterableUtil_format_Test {

  @Test
  public void should_return_null_if_iterable_is_null() {
    assertThat(IterableUtil.format(STANDARD_REPRESENTATION, null)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_iterable_is_empty() {
    assertThat(IterableUtil.format(STANDARD_REPRESENTATION, asList())).isEqualTo("[]");
  }

  @Test
  public void should_format_iterable() {
    List<? extends Object> list = asList("First", 3);
    assertThat(IterableUtil.format(STANDARD_REPRESENTATION, list)).isEqualTo("[\"First\", 3]");
  }

  @Test
  public void should_format_iterable_with_custom_start_and_end() {
    List<? extends Object> list = asList("First", 3);
    assertThat(IterableUtil.format(STANDARD_REPRESENTATION, list, "{", "}")).isEqualTo("{\"First\", 3}");
    assertThat(IterableUtil.format(STANDARD_REPRESENTATION, asList(), "{", "}")).isEqualTo("{}");
  }

  @Test
  public void should_format_iterable_with_an_element_per_line() {
    String formatted = multiLineFormat(STANDARD_REPRESENTATION, asList("First", 3, "foo", "bar"));
    String formattedAfterNewLine = System.lineSeparator() + "  <" + formatted + ">";
    assertThat(formattedAfterNewLine).isEqualTo(format("%n" +
                                                       "  <[\"First\",%n" +
                                                       "    3,%n" +
                                                       "    \"foo\",%n" +
                                                       "    \"bar\"]>"));
  }

  @Test
  public void should_format_iterable_with_an_element_per_line_according_the_given_representation() {
    String formatted = multiLineFormat(HEXA_REPRESENTATION, asList(1, 2, 3));
    String formattedAfterNewLine = System.lineSeparator() + "  <" + formatted + ">";
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
    String formatted = multiLineFormat(STANDARD_REPRESENTATION, list);
    assertThat(formatted).isEqualTo(format("[(this Collection),%n" +
                                           "    (this Collection)]"));
  }

}
