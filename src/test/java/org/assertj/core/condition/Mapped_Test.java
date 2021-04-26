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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

public class Mapped_Test {

  private static final String NL = System.lineSeparator();

  private static final String INNER_CONDITION_DESCRIPTION = "isString and BAR";

  private static final String BAR = "bar";

  private static final String FOO = "foo";

  final static Condition<String> innerCondition = new Condition<>((s) -> BAR.equals(s) && s instanceof String,
      INNER_CONDITION_DESCRIPTION);

  final static String text_BAR = "mapped" + NL + "   using: ::toString" + NL + "   from: <StringBuilder> " + BAR + ""
      + NL + "   to:   <String> " + BAR + "" + NL + "   then checked: [" + NL + "      "
      + INNER_CONDITION_DESCRIPTION + "" + NL + "]";

  final static String text_BAR_PLAIN = "mapped" + NL + "   from: <StringBuilder> " + BAR + "" + NL
      + "   to:   <String> " + BAR + "" + NL + "   then checked: [" + NL + "      " + INNER_CONDITION_DESCRIPTION
      + "" + NL + "]";

  final static String text_FOO = "mapped" + NL + "   using: ::toString" + NL + "   from: <StringBuilder> " + FOO + ""
      + NL + "   to:   <String> " + FOO + "" + NL + "   then checked: [" + NL + "      "
      + INNER_CONDITION_DESCRIPTION + "" + NL + "]";

  @Test
  void MappedCondition_mapped_withDescription_works() {

    Condition<StringBuilder> mappedCondition = Mapped.mapped(StringBuilder::toString, "::toString", innerCondition);

    assertThat(mappedCondition.matches(new StringBuilder(BAR))).isTrue();
    assertThat(mappedCondition.toString()).isEqualTo(text_BAR);
    assertThat(mappedCondition.matches(new StringBuilder(FOO))).isFalse();
    assertThat(mappedCondition.toString()).isEqualTo(text_FOO);
  }

  @Test
  void MappedCondition_mapped_withoutDescription_works() {
    Condition<StringBuilder> mappedCondition2 = Mapped.mapped(StringBuilder::toString, innerCondition);
    assertThat(mappedCondition2.matches(new StringBuilder(BAR))).isTrue();
    assertThat(mappedCondition2.toString()).isEqualTo(text_BAR_PLAIN);
  }

  @Test
  void MappedCondition_mapped_withDescription_conditionIsNull_throws_NPE() {
    assertThatNullPointerException().isThrownBy(() -> {
      Mapped.mapped(StringBuilder::toString, "::toString", null);
    }).withMessage("The given conditions should not be null");
  }

  @Test
  void MappedCondition_mapped_withDescription_MappingIsNull_throws_NPE() {
    assertThatNullPointerException().isThrownBy(() -> {
      Mapped.mapped(null, "::toString", innerCondition);
    }).withMessage("The given mapping should not be null");
  }

  @Test
  void MappedCondition_mapped_WithoutDescription_conditionIsNull_throws_NPE() {
    assertThatNullPointerException().isThrownBy(() -> {
      Mapped.mapped(StringBuilder::toString, null);
    }).withMessage("The given conditions should not be null");
  }

  @Test
  void MappedCondition_mapped_WithoutDescription_mappingIsNull_throws_NPE() {
    assertThatNullPointerException().isThrownBy(() -> {
      Mapped.mapped(null, innerCondition);
    }).withMessage("The given mapping should not be null");
  }
  
  private final Condition<String> existingCondition = new Condition<>(t -> t.contains(System.lineSeparator()),
      "has lineSeparator");

  @Test
  private void example() {

    Optional<String> value = Optional.of("a" + System.lineSeparator());
    Mapped<Optional<String>, String> mappedCondition = Mapped.mapped(Optional::get, existingCondition);
    boolean bool= mappedCondition.matches(value);

    assertThat(bool).isTrue();
  }
}
