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
package org.assertj.core.description;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JoinDescription#value()}.
 *
 * @author Edgar Asatryan
 */
@DisplayName("JoinDescription value()")
class JoinDescription_value_Test {

  @Test
  void should_not_use_newline_when_empty() {
    // GIVEN
    JoinDescription joinDescription = allOf(); // with no descriptions
    // WHEN
    String actual = joinDescription.value();
    // THEN
    assertThat(actual).isEqualTo("all of:[]");
  }

  @Test
  void should_use_new_line_when_non_empty() {
    // GIVEN
    JoinDescription joinDescription = allOf(description("1"), description("2"));
    // WHEN
    String actual = joinDescription.value();
    // THEN
    assertThat(actual).isEqualTo(format("all of:[%n" +
                                        "   1,%n" +
                                        "   2%n" +
                                        "]"));
  }

  @Test
  void should_indent_nested_join_descriptions() {
    // GIVEN
    JoinDescription joinDescription = allOf(description("1"),
                                            anyOf(allOf(description("2"),
                                                        anyOf(description("3a"), description("3b"))),
                                                  description("4"),
                                                  description("5")));
    // WHEN
    String actual = joinDescription.value();
    // THEN
    assertThat(actual).isEqualTo(format("all of:[%n" +
                                        "   1,%n" +
                                        "   any of:[%n" +
                                        "      all of:[%n" +
                                        "         2,%n" +
                                        "         any of:[%n" +
                                        "            3a,%n" +
                                        "            3b%n" +
                                        "         ]%n" +
                                        "      ],%n" +
                                        "      4,%n" +
                                        "      5%n" +
                                        "   ]%n" +
                                        "]"));
  }

  private static Description description(String desc) {
    return new TestDescription(desc);
  }

  private static JoinDescription allOf(Description... descriptions) {
    return new JoinDescription("all of:[", "]", list(descriptions));
  }

  private static JoinDescription anyOf(Description... descriptions) {
    return new JoinDescription("any of:[", "]", list(descriptions));
  }
}
