package org.assertj.core.api.filter;

import org.assertj.core.api.Condition;
import org.assertj.core.testkit.WithPlayerData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.filter;

class Filter_with_matches_Test extends WithPlayerData {
  @Test
  void should_filter_players_that_match_Condition() {
    Condition<Integer> greaterThan20 = new Condition<>(i -> i > 20, "> 20");
    Condition<Integer> greaterThan7 = new Condition<>(i -> i > 7, "> 7");

    assertThat(
               filter(players)
                              .with("pointsPerGame").matches(greaterThan20)
                              .with("assistsPerGame").matches(greaterThan7)
                              .get()).containsOnly(jordan);
  }

  @Test
  void should_fail_on_null_condition() {
    Condition<Integer> nullCondition = null;

    assertThatThrownBy(() -> filter(players).with("pointsPerGame").matches(nullCondition))
                                                                                          .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_fail_on_mismatched_condition_type() {
    Condition<String> stringCondition = new Condition<>(s -> s.length() > 3, "length > 3");

    assertThatThrownBy(() -> filter(players).with("pointsPerGame").matches(stringCondition))
                                                                                            .isInstanceOf(IllegalArgumentException.class)
                                                                                            .hasMessageContaining("Condition type does not match the property type.");
  }
}
