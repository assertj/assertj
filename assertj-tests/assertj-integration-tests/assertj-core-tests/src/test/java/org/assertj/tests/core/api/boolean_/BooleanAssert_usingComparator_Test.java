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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.boolean_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Comparator;
import org.junit.jupiter.api.Test;

class BooleanAssert_usingComparator_Test {

  @Test
  @SuppressWarnings("deprecation")
  void should_prevent_using_comparator_for_boolean_assertions() {
    // GIVEN
    Comparator<Boolean> comparator = (o1, o2) -> 0;
    // WHEN
    Exception exception = catchException(() -> assertThat(true).usingComparator(comparator));
    // THEN
    then(exception).isInstanceOf(UnsupportedOperationException.class);
  }

}
