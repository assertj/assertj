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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatComparable;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Assertions_assertThatComparable_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    SomeComparable comparable = new SomeComparable();
    // WHEN
    AbstractComparableAssert<?, SomeComparable> assertions = assertThatComparable(comparable);
    // THEN
    then(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    SomeComparable comparable = new SomeComparable();
    // WHEN
    AbstractComparableAssert<?, SomeComparable> assertions = assertThatComparable(comparable);
    // THEN
    then(assertions.actual).isSameAs(comparable);
  }

  private static class SomeComparable implements Comparable<SomeComparable> {
    @Override
    public int compareTo(SomeComparable o) {
      return 0;
    }
  }
}
