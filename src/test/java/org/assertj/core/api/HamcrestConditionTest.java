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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

public class HamcrestConditionTest {

  @Test
  public void should_be_able_to_use_a_hamcrest_matcher_as_a_condition() {
    Condition<String> aStringContainingA = new HamcrestCondition<>(containsString("a"));

    assertThat("abc").is(aStringContainingA)
                     .has(aStringContainingA)
                     .satisfies(aStringContainingA);
    assertThat("bc").isNot(aStringContainingA);
  }

  @Test
  public void should_be_able_to_use_a_hamcrest_matcher_with_generic() {
    Collection<? extends CharSequence> emptyIterable = Collections.emptyList();
    Collection<? extends CharSequence> oneElementIterable = Collections.singletonList("item");
    assertThat(emptyIterable).is(new HamcrestCondition<>(empty()))
      .has(new HamcrestCondition<>(empty()))
      .satisfies(new HamcrestCondition<>(empty()));
    assertThat(oneElementIterable).isNot(new HamcrestCondition<>(empty()));
  }
}
