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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JoinDescription#value()}.
 *
 * @author Edgar Asatryan
 */
class JoinDescription_value_Test {
  private static Description desc(String desc) {
    return new TestDescription(desc);
  }

  private static JoinDescription joinDescription(Description... descriptions) {

    return new JoinDescription("all of:<[", "]>", list(descriptions));
  }

  @Test
  void should_not_use_newline_when_empty() {
    assertThat(joinDescription().value()).isEqualTo("all of:<[]>");
  }

  @Test
  void should_use_new_line_when_non_empty() {
    JoinDescription joinDescription = joinDescription(desc("1"), desc("2"));

    assertThat(joinDescription.value()).isEqualTo(String.format("all of:<[%n" +
      "   1,%n" +
      "   2%n" +
      "]>"));
  }

  @Test
  void should_indent_nested_join_descriptions() {
    JoinDescription joinDescription = joinDescription(desc("1"), joinDescription(joinDescription(desc("2"))));

    assertThat(joinDescription.value()).isEqualTo(String.format("all of:<[%n" +
      "   1,%n" +
      "   all of:<[%n" +
      "      all of:<[%n" +
      "         2%n" +
      "      ]>%n" +
      "   ]>%n" +
      "]>"));
  }
}
