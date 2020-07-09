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
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

public class DualValue_isActualJavaType_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  public void isActualJavaType_should_return_true_when_actual_is_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", "");
    // WHEN
    boolean expectedFieldIsArray = dualValue.isActualJavaType();
    // THEN
    assertThat(expectedFieldIsArray).isTrue();
  }

  @Test
  public void isActualJavaType_should_return_false_when_actual_is_not_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), "");
    // WHEN
    boolean expectedFieldIsArray = dualValue.isActualJavaType();
    // THEN
    assertThat(expectedFieldIsArray).isFalse();
  }

  @Test
  public void isActualJavaType_should_return_false_when_actual_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "");
    // WHEN
    boolean expectedFieldIsArray = dualValue.isActualJavaType();
    // THEN
    assertThat(expectedFieldIsArray).isFalse();
  }

}
