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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class DualValue_isExpectedJavaType_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void isExpectedJavaType_should_return_true_when_expected_is_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), "");
    // WHEN
    boolean isExpectedJavaType = dualValue.isExpectedJavaType();
    // THEN
    then(isExpectedJavaType).isTrue();
  }

  @Test
  void isExpectedJavaType_should_return_false_when_expected_is_not_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", Pair.of(1, "a"));
    // WHEN
    boolean isExpectedJavaType = dualValue.isExpectedJavaType();
    // THEN
    then(isExpectedJavaType).isFalse();
  }

  @Test
  void isExpectedJavaType_should_return_false_when_expected_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", null);
    // WHEN
    boolean isExpectedJavaType = dualValue.isExpectedJavaType();
    // THEN
    then(isExpectedJavaType).isFalse();
  }

}
