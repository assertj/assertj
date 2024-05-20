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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

class DualValue_hasSomeJavaTypeValue_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void hasSomeJavaTypeValue_should_return_true_when_actual_is_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", Pair.of(1, "a"));
    // WHEN
    boolean hasSomeJavaTypeValue = dualValue.hasSomeJavaTypeValue();
    // THEN
    then(hasSomeJavaTypeValue).isTrue();
  }

  @Test
  void hasSomeJavaTypeValue_should_return_true_when_expected_is_a_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), "");
    // WHEN
    boolean hasSomeJavaTypeValue = dualValue.hasSomeJavaTypeValue();
    // THEN
    then(hasSomeJavaTypeValue).isTrue();
  }

  @Test
  void hasSomeJavaTypeValue_should_return_true_when_both_values_are_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "1", "2");
    // WHEN
    boolean hasSomeJavaTypeValue = dualValue.hasSomeJavaTypeValue();
    // THEN
    then(hasSomeJavaTypeValue).isTrue();
  }

  @Test
  void hasSomeJavaTypeValue_should_return_true_when_no_values_are_java_type() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), Pair.of(1, "b"));
    // WHEN
    boolean hasSomeJavaTypeValue = dualValue.hasSomeJavaTypeValue();
    // THEN
    then(hasSomeJavaTypeValue).isFalse();
  }

  @Test
  void hasSomeJavaTypeValue_should_return_false_when_both_values_are_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, null);
    // WHEN
    boolean hasSomeJavaTypeValue = dualValue.hasSomeJavaTypeValue();
    // THEN
    then(hasSomeJavaTypeValue).isFalse();
  }

}
