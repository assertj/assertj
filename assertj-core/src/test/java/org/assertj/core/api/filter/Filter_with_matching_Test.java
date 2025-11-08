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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.filter;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.filter;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.assertj.core.api.Condition;
import org.assertj.core.testkit.Player;
import org.assertj.core.testkit.WithPlayerData;
import org.junit.jupiter.api.Test;

class Filter_with_matching_Test extends WithPlayerData {
  @Test
  void should_filter_players_matching_condition() {
    // GIVEN
    Condition<Integer> greaterThan20 = new Condition<>(i -> i > 20, "> 20");
    Condition<Integer> greaterThan7 = new Condition<>(i -> i > 7, "> 7");
    // WHEN
    List<Player> mvps = filter(players).with("pointsPerGame").matching(greaterThan20)
                                       .with("assistsPerGame").matching(greaterThan7)
                                       .get();
    // THEN
    then(mvps).containsOnly(jordan);
  }

  @Test
  void should_fail_on_null_condition() {
    // GIVEN
    Condition<Integer> nullCondition = null;
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> filter(players).with("pointsPerGame")
                                                                                      .matching(nullCondition));
    // THEN
    then(illegalArgumentException).hasMessage("The filter condition should not be null");
  }

  @Test
  void should_fail_on_mismatched_condition_type() {
    // GIVEN
    Condition<String> stringCondition = new Condition<>(s -> s.length() > 3, "length > 3");
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> filter(players).with("pointsPerGame")
                                                                                      .matching(stringCondition));
    // THEN
    then(illegalArgumentException).hasMessageContaining("Condition type does not match the property type.");
  }
}
