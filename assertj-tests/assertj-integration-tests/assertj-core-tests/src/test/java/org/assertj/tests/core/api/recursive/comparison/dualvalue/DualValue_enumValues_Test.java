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
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.api.recursive.data.Color.BLUE;
import static org.assertj.tests.core.api.recursive.data.Color.RED;

import java.util.List;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

class DualValue_enumValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void isExpectedAnEnum_should_return_true_when_expected_is_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", BLUE);
    // WHEN
    boolean isExpectedAnEnum = dualValue.isExpectedAnEnum();
    // THEN
    then(isExpectedAnEnum).isTrue();
  }

  @Test
  void isExpectedAnEnum_should_return_false_when_expected_is_not_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, RED, "");
    // WHEN
    boolean isExpectedAnEnum = dualValue.isExpectedAnEnum();
    // THEN
    then(isExpectedAnEnum).isFalse();
  }

  @Test
  void isExpectedAnEnum_should_return_false_when_expected_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, RED, null);
    // WHEN
    boolean isExpectedAnEnum = dualValue.isExpectedAnEnum();
    // THEN
    then(isExpectedAnEnum).isFalse();
  }

  @Test
  void isActualAnEnum_should_return_true_when_actual_is_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, BLUE, "");
    // WHEN
    boolean isActualAnEnum = dualValue.isActualAnEnum();
    // THEN
    then(isActualAnEnum).isTrue();
  }

  @Test
  void isActualAnEnum_should_return_false_when_actual_is_not_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", RED);
    // WHEN
    boolean isActualAnEnum = dualValue.isActualAnEnum();
    // THEN
    then(isActualAnEnum).isFalse();
  }

  @Test
  void isActualAnEnum_should_return_false_when_actual_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "");
    // WHEN
    boolean actualIsEnum = dualValue.isActualAnEnum();
    // THEN
    then(actualIsEnum).isFalse();
  }

}
