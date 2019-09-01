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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JoinDescription}.
 *
 * @author Edgar Asatryan
 */
class JoinDescriptionTest {
  private static Description desc(String desc) {
    return new TestDescription(desc);
  }

  private static JoinDescription joinDescription(Description... descriptions) {
    return new JoinDescription("all of:<[", "]>", Arrays.asList(descriptions));
  }

  /**
   * Tests for {@link JoinDescription#JoinDescription(String, String, Collection)}.
   */
  @Nested
  @SuppressWarnings("InnerClassMayBeStatic")
    // IntelliJ 2019.2
  class Constructor {
    @Test
    void should_set_values() {
      String prefix = "a";
      String suffix = "b";
      Collection<Description> descriptions = Arrays.asList(new TestDescription("1"), new TestDescription("2"));

      JoinDescription desc = new JoinDescription(prefix, suffix, descriptions);

      assertThat(desc.prefix).isEqualTo(prefix);
      assertThat(desc.suffix).isEqualTo(suffix);
      assertThat(desc.descriptions).isEqualTo(descriptions);
    }

    @Test
    void should_throw_when_descriptions_contains_null() {
      List<Description> descriptions = Arrays.asList(
        new TestDescription("1"),
        new TestDescription("2"),
        null,
        new TestDescription("3")
      );
      assertThatNullPointerException()
        .isThrownBy(() -> new JoinDescription("a", "b", descriptions))
        .withMessage("The descriptions should not contain null elements");
    }
  }

  /**
   * Tests for {@link JoinDescription#value()}.
   */
  @Nested
  @SuppressWarnings("InnerClassMayBeStatic")
    // IntelliJ 2019.2
  class Value {
    @Test
    void should_not_use_newline_when_empty() {
      assertThat(joinDescription().value()).isEqualTo("all of:<[]>");
    }

    @Test
    void should_use_new_line_when_non_empty() {
      JoinDescription joinDescription = joinDescription(desc("1"), desc("2"));

      assertThat(joinDescription.value()).isEqualTo("all of:<[\n" +
        "  1,\n" +
        "  2\n" +
        "]>");
    }

    @Test
    void should_indent_nested_join_descriptions() {
      JoinDescription joinDescription = joinDescription(desc("1"), joinDescription(joinDescription(desc("2"))));

      assertThat(joinDescription.value()).isEqualTo("all of:<[\n" +
        "  1,\n" +
        "  all of:<[\n" +
        "    all of:<[\n" +
        "      2\n" +
        "    ]>\n" +
        "  ]>\n" +
        "]>");
    }
  }
}
