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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.Color.BLUE;
import static org.assertj.core.api.recursive.comparison.Color.RED;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DualValue_enumValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  public void isExpectedAnEnum_should_return_true_when_expected_is_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", BLUE);
    // WHEN
    boolean expectedFieldIsEnum = dualValue.isExpectedAnEnum();
    // THEN
    assertThat(expectedFieldIsEnum).isTrue();
  }

  @Test
  public void isExpectedAnEnum_should_return_false_when_expected_is_not_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, RED, "");
    // WHEN
    boolean expectedFieldIsEnum = dualValue.isExpectedAnEnum();
    // THEN
    assertThat(expectedFieldIsEnum).isFalse();
  }

  @Test
  public void isExpectedAnEnum_should_return_false_when_expected_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "");
    // WHEN
    boolean expectedFieldIsEnum = dualValue.isExpectedAnEnum();
    // THEN
    assertThat(expectedFieldIsEnum).isFalse();
  }

  @Test
  public void isActualAnEnum_should_return_true_when_actual_is_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, BLUE, "");
    // WHEN
    boolean actualFieldIsEnum = dualValue.isActualAnEnum();
    // THEN
    assertThat(actualFieldIsEnum).isTrue();
  }

  @Test
  public void isActualAnEnum_should_return_false_when_actual_is_not_an_enum() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", RED);
    // WHEN
    boolean actualFieldIsEnum = dualValue.isActualAnEnum();
    // THEN
    assertThat(actualFieldIsEnum).isFalse();
  }

  @Test
  public void isActualAnEnum_should_return_false_when_actual_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "");
    // WHEN
    boolean actualIsEnum = dualValue.isActualAnEnum();
    // THEN
    assertThat(actualIsEnum).isFalse();
  }

}
