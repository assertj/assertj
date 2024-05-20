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
package org.assertj.core.api.boolean_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import java.util.Comparator;
import org.junit.jupiter.api.Test;

class BooleanAssert_usingComparator_Test {

  @Test
  @SuppressWarnings("deprecation")
  void should_prevent_using_comparator_for_boolean_assertions() {
    // GIVEN
    // we don't care of the comparator, the point to check is that we can't use a comparator
    Comparator<Boolean> comparator = alwaysEqual();
    // WHEN/THEN
    assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> assertThat(true).usingComparator(comparator));
  }
}
