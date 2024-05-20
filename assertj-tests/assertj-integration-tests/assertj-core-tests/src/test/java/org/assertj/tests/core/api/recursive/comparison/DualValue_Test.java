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

import static java.util.Objects.hash;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.Objects;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class DualValue_Test {

  @Test
  void should_honor_equals_contract() {
    EqualsVerifier.forClass(DualValue.class)
                  .withNonnullFields("fieldLocation")
                  .withCachedHashCode("hashCode", "computeHashCode", new DualValue(list(), "foo", "bar"))
                  .verify();
  }

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
      return hash(changeMe);
    }
  }

  @Test
  void should_return_same_hashcode_after_mutation() {
    // GIVEN
    MutableType actual = new MutableType("One");
    MutableType expected = new MutableType("One");
    DualValue dualValue1 = new DualValue(list(), actual, expected);
    DualValue dualValue2 = new DualValue(list(), actual, expected);
    int hashCodeBeforeMutation = dualValue1.hashCode();
    // WHEN
    actual.setChangeMe("Another value");
    // THEN
    then(dualValue1).isEqualTo(dualValue2)
                    .hasSameHashCodeAs(dualValue2);
    int hashCodeAfterMutation = dualValue1.hashCode();
    then(hashCodeAfterMutation).isEqualTo(hashCodeBeforeMutation);
  }

}
