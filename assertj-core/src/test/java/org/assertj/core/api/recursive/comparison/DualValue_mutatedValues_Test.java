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
package org.assertj.core.api.recursive.comparison;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class DualValue_mutatedValues_Test {

  private static final class MutableType {
    String changeMe;
    private MutableType(String startingValue) {
      changeMe = startingValue;
    }

    private void setChangeMe(String nextValue) {
      changeMe = nextValue;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      MutableType that = (MutableType) o;
      return Objects.equals(changeMe, that.changeMe);
    }

    @Override
    public int hashCode() {
      return Objects.hash(changeMe);
    }
  }

  @Test
  void isMutableValue_should_return_same_hashcode_after_mutation() {
    // GIVEN
    MutableType actual = new MutableType("One");
    MutableType expected = new MutableType("One");
    DualValue dualValue1 = new DualValue(Lists.list("changeMe"), actual, expected);
    // WHEN
    actual.setChangeMe("Another value");
    DualValue dualValue2 = new DualValue(Lists.list("changeMe"), actual, expected);
    // THEN
    assertThat(dualValue1)
      .isEqualTo(dualValue2)
      .hasSameHashCodeAs(dualValue2);
  }

}
