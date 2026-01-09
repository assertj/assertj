/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_DIFFERENT;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_EQUALS;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class ObjectAssert_usingEquals_Test {

  @Test
  void should_be_able_to_use_a_custom_equals_comparison() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "green");
    Jedi luke = new Jedi("Luke", "green");
    // WHEN/THEN
    then(yoda).usingEquals(ALWAYS_EQUALS).isEqualTo(luke);
    then(yoda).usingEquals(ALWAYS_EQUALS, "always equals").isEqualTo(luke);
    then(yoda).usingEquals(ALWAYS_DIFFERENT).isNotEqualTo(luke);
    then(yoda).usingEquals(ALWAYS_DIFFERENT, "always different").isNotEqualTo(luke);
  }

  @Test
  void should_include_custom_equals_description_in_error_message() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "green");
    Jedi luke = new Jedi("Luke", "green");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(yoda).usingEquals((o1, o2) -> o1.getName()
                                                                                               .equals(o2.getName()),
                                                                                 "comparing names")
                                                                    .isEqualTo(luke));
    // THEN
    then(assertionError).hasMessageContaining("comparing names");
  }

  @Test
  void should_reset_custom_equals_to_default() {
    // GIVEN
    Jedi yoda1 = new Jedi("Yoda", "green");
    Jedi yoda2 = new Jedi("Yoda", "green");
    // WHEN/THEN
    then(yoda1).usingEquals(ALWAYS_DIFFERENT)
               .usingDefaultComparator()
               .isEqualTo(yoda2);
    then(yoda2).usingEquals(ALWAYS_DIFFERENT, "always different")
               .usingDefaultComparator()
               .isEqualTo(yoda1);
  }

}
