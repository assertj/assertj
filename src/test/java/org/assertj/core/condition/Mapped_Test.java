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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class Mapped_Test {

  private static final String NL = System.lineSeparator();

  private static final String INNER_CONDITION_DESCRIPTION = "isString and BAR";

  private static final String BAR = "bar";

  private static final String FOO = "foo";

  final static Condition<String> innerCondition = new Condition<>(
      (s) -> BAR.equals(s) && s instanceof String, INNER_CONDITION_DESCRIPTION);

  final static String text_BAR = "mapped" + NL + "   using: ::toString" + NL + "   from: <StringBuilder> " + BAR + ""
      + NL + "   to:   <String> " + BAR + "" + NL + "   then checked: [" + NL + "      "
      + INNER_CONDITION_DESCRIPTION + "" + NL + "]";

  final static String text_BAR_PLAIN = "mapped" + NL + "   from: <StringBuilder> " + BAR
      + "" + NL + "   to:   <String> " + BAR + "" + NL + "   then checked: [" + NL + "      "
      + INNER_CONDITION_DESCRIPTION + "" + NL + "]";

  final static String text_FOO = "mapped" + NL + "   using: ::toString" + NL + "   from: <StringBuilder> " + FOO + ""
      + NL + "   to:   <String> " + FOO + "" + NL + "   then checked: [" + NL + "      "
      + INNER_CONDITION_DESCRIPTION + "" + NL + "]";

  @Test
  void MappedCondition_works() {

    Condition<StringBuilder> mappedCondition = Mapped.mapped(StringBuilder::toString, "::toString",
        innerCondition);

    assertThat(mappedCondition.matches(new StringBuilder(BAR))).isTrue();
    assertThat(mappedCondition.toString()).isEqualTo(text_BAR);
    assertThat(mappedCondition.matches(new StringBuilder(FOO))).isFalse();
    assertThat(mappedCondition.toString()).isEqualTo(text_FOO);
    
    Condition<StringBuilder> mappedCondition2 = Mapped.mapped(StringBuilder::toString, innerCondition);
    assertThat(mappedCondition2.matches(new StringBuilder(BAR))).isTrue();
    assertThat(mappedCondition2.toString()).isEqualTo(text_BAR_PLAIN);
  }
  
  @Test
  void MappedCondition_throws_NPE() {

    assertThrows(NullPointerException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        Mapped.mapped(StringBuilder::toString, "::toString", null);
      }
    });

    assertThrows(NullPointerException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        Mapped.mapped(null, "::toString", innerCondition);
      }
    });

    assertThrows(NullPointerException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        Mapped.mapped(StringBuilder::toString, null);
      }
    });

    assertThrows(NullPointerException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        Mapped.mapped(null, innerCondition);
      }
    });
  }
}
