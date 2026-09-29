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
import static org.assertj.tests.core.api.recursive.data.DualValueUtil.randomFieldLocation;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

class DualValue_isKeyMapDualValue_Test {

  @Test
  void should_return_false_by_default() {
    // GIVEN
    var dualValue = new DualValue(randomFieldLocation(), "", "foo", null);
    // WHEN
    boolean isKeyMapDualValue = dualValue.isKeyMapDualValue();
    // THEN
    then(isKeyMapDualValue).isFalse();
  }

  @Test
  void should_return_true() {
    // GIVEN
    var dualValue = new DualValue(randomFieldLocation(), "", "foo", null, true);
    // WHEN
    boolean isKeyMapDualValue = dualValue.isKeyMapDualValue();
    // THEN
    then(isKeyMapDualValue).isTrue();
  }

  @Test
  void should_return_true_for_child_of_keyMapDualValue() {
    // GIVEN
    var dualValue = new DualValue(randomFieldLocation(), "", "foo", null, true);
    var childDualValue = new DualValue(randomFieldLocation(), "", "foo", dualValue);
    // WHEN
    boolean isKeyMapDualValue = childDualValue.isKeyMapDualValue();
    // THEN
    then(isKeyMapDualValue).isTrue();
  }

}
