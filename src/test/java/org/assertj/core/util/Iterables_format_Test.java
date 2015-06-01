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
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Iterables_format_Test {

  @Test
  public void should_return_null_if_Collection_is_null() {
    assertThat(Iterables.format(STANDARD_REPRESENTATION, null)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_Collection_is_empty() {
    assertThat(Iterables.format(STANDARD_REPRESENTATION, new ArrayList<String>())).isEqualTo("[]");
    // custom start and end
    assertThat(Iterables.format(STANDARD_REPRESENTATION, new ArrayList<String>(), ">", "<")).isEqualTo("><");
  }

  @Test
  public void should_format_Collection() {
    List<? extends Object> list = asList("First", 3);
    assertThat(Iterables.format(STANDARD_REPRESENTATION, list)).isEqualTo("[\"First\", 3]");
  }

  @Test
  public void should_format_Collection_with_an_element_per_line() {
    List<? extends Object> list = asList("First", 3, "foo", "bar");
    String formatted = Iterables.multiLineFormat(STANDARD_REPRESENTATION, list);
    String formattedAfterNewLine = System.lineSeparator() + "  <" + formatted + ">";
    assertThat(formattedAfterNewLine).isEqualTo(format("%n" +
                                                       "  <[\"First\",%n" +
                                                       "    3,%n" +
                                                       "    \"foo\",%n" +
                                                       "    \"bar\"]>"));
  }
}
