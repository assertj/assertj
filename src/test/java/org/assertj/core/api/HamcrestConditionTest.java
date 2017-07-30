/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import org.assertj.core.api.Condition;
import org.assertj.core.api.HamcrestCondition;
import org.junit.Test;

public class HamcrestConditionTest {

  @Test
  public void should_be_able_to_use_a_hamcrest_matcher_as_a_condition() {
    Condition<String> aStringContainingA = new HamcrestCondition<>(containsString("a"));

    assertThat("abc").is(aStringContainingA);
    assertThat("bc").isNot(aStringContainingA);
  }
}
