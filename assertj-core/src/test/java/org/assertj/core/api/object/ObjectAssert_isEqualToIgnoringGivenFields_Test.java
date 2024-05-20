/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.object;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#isEqualToIgnoringGivenFields(Object, String...)}</code>.
 *
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
@SuppressWarnings("deprecation")
class ObjectAssert_isEqualToIgnoringGivenFields_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Blue");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.isEqualToIgnoringGivenFields(other, "lightSaberColor");
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void verify_internal_effects() {
    verify(objects).assertIsEqualToIgnoringGivenFields(getInfo(assertions), getActual(assertions), other,
                                                       EMPTY_MAP, defaultTypeComparators(), "lightSaberColor");
  }

  @Test
  void should_be_able_to_use_a_comparator_for_specified_fields() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Blue");

    assertThat(actual).usingComparatorForFields(ALWAYS_EQUALS_STRING, "name")
                      .isEqualToIgnoringGivenFields(other, "lightSaberColor");
  }

  @Test
  void comparators_for_fields_should_have_precedence_over_comparators_for_types() {
    Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(actual).usingComparatorForFields(ALWAYS_EQUALS_STRING, "name")
                      .usingComparatorForType(comparator, String.class)
                      .isEqualToIgnoringGivenFields(other, "lightSaberColor");
  }

  @Test
  void should_be_able_to_use_a_comparator_for_specified_type() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "blue");

    assertThat(actual).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                      .isEqualToIgnoringGivenFields(other, "lightSaberColor");
  }

}
