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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_DIFFERENT;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_EQUALS;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#isEqualTo(Object)} (Object)}</code>.
 */
class ObjectAssert_isEqualsTo_usingEquals_Test {

  private final Jedi other = new Jedi("Yoda", "Blue");

  @Test
  void should_be_able_to_use_a_custom_equals() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(actual)
                      .usingEquals(ALWAYS_EQUALS)
                      .isEqualTo(other);
    assertThat(actual)
                      .usingEquals(ALWAYS_EQUALS, "name")
                      .isEqualTo(other);
    assertThat(actual)
                      .usingEquals(ALWAYS_DIFFERENT)
                      .isNotEqualTo(other);
    assertThat(actual)
                      .usingEquals(ALWAYS_DIFFERENT, "name")
                      .isNotEqualTo(other);
  }

  @Test
  void should_reset_custom_equals_to_default() {
    Jedi actual = new Jedi("Yoda", "Blue");
    Jedi luke = new Jedi("Luke", "green");

    assertThat(actual)
                      .usingEquals(ALWAYS_EQUALS)
                      .usingDefaultComparator()
                      .isEqualTo(other);
    assertThat(actual)
                      .usingEquals(ALWAYS_EQUALS, "name")
                      .usingDefaultComparator()
                      .isEqualTo(other);
    assertThat(actual)
                      .usingEquals(ALWAYS_DIFFERENT)
                      .usingDefaultComparator()
                      .isNotEqualTo(luke);
    assertThat(actual)
                      .usingEquals(ALWAYS_DIFFERENT, "name")
                      .usingDefaultComparator()
                      .isNotEqualTo(luke);
  }

}
