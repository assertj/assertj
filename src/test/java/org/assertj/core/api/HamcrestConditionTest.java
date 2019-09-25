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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.HamcrestCondition.matching;
import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.StringContains.containsString;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class HamcrestConditionTest {

  @Test
  public void should_be_able_to_use_a_hamcrest_matcher_as_a_condition() {
    // GIVEN
    Condition<String> aStringContainingA = new HamcrestCondition<>(containsString("a"));
    // THEN
    assertThat("abc").is(aStringContainingA)
                     .has(aStringContainingA)
                     .satisfies(aStringContainingA);
    assertThat("bc").isNot(aStringContainingA);
    assertThat("abc").is(matching(containsString("a")));
  }

  @Test
  public void should_be_able_to_use_a_hamcrest_matcher_with_generic() {
    // GIVEN
    Collection<? extends CharSequence> emptyIterable = list();
    Collection<? extends CharSequence> oneElementIterable = list("item");
    // THEN
    assertThat(emptyIterable).is(new HamcrestCondition<>(empty()))
                             .has(new HamcrestCondition<>(empty()))
                             .satisfies(new HamcrestCondition<>(empty()));
    assertThat(oneElementIterable).isNot(new HamcrestCondition<>(empty()));
  }
}
