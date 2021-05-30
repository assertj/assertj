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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.condition;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VerboseConditionTest {

  private static final Condition<String> VERBOSE_C_WITH_TRANSFORM = VerboseCondition
      .verboseCondition(actual -> actual.length() < 4, "word must be shorter than <4>",
          (s) -> String.format("%s (original word: %s)", s.length(), s));

  @Test
  public void test_verboseCondition_OWithTransformations_maches_executed_description_when_matching() {

    Condition<String> condition = VERBOSE_C_WITH_TRANSFORM;
    then(condition.matches("foo")).isTrue();
    then(condition).hasToString("word must be shorter than <4>");
  }

  @Test
  public void test_verboseCondition_WithTransformations_maches_executed_description_when_not_matching() {

    Condition<String> condition = VERBOSE_C_WITH_TRANSFORM;
    then(condition.matches("foooo")).isFalse();
    then(condition).hasToString("word must be shorter than <4> but was <5 (original word: foooo)>");
  }

  @Test
  public void test_matchesBiPredicate_null_throws_exception() {

    assertThatNullPointerException().isThrownBy(() -> {
      VerboseCondition.verboseCondition(null, null, (t) -> "");
    });
  }

}
