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
 * Copyright 2012-2021 the original author or authors.
 */
/**
 * 
 */
package org.assertj.core.condition;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

public class Mapped_Test {

  private static final String INNER_CONDITION_DESCRIPTION = "isString and BAR";

  private static final String BAR = "bar";

  private static final String FOO = "foo";

  final static Condition<String> innerCondition = new Condition<>(
      (s) -> BAR.equals(s) && s instanceof String, INNER_CONDITION_DESCRIPTION);

  final static String text_BAR = "mapped\n" + "   using: ::toString\n" + "   from: <StringBuilder> "
      + BAR + "\n" + "   to:   <String> " + BAR + "\n" + "   then checked: [\n" + "      "
      + INNER_CONDITION_DESCRIPTION + "\n" + "]";

  final static String text_FOO = "mapped\n" + "   using: ::toString\n" + "   from: <StringBuilder> "
      + FOO + "\n" + "   to:   <String> " + FOO + "\n" + "   then checked: [\n" + "      "
      + INNER_CONDITION_DESCRIPTION + "\n" + "]";

  @Test
  public static void MappedCondition_works_with_Of() {

    Condition<StringBuilder> mappedCondition = Mapped.mapped(StringBuilder::toString, "::toString",
        innerCondition);

    assertThat(mappedCondition.matches(new StringBuilder(BAR))).isTrue();
    assertThat(mappedCondition.toString()).isSameAs(text_BAR);
    assertThat(mappedCondition.matches(new StringBuilder(FOO))).isFalse();
    assertThat(mappedCondition.toString()).isSameAs(text_FOO);

  }
}
