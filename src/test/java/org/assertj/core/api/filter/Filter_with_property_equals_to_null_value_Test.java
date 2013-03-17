package org.assertj.core.api.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;

import org.assertj.core.test.Player;
import org.junit.Test;


public class Filter_with_property_equals_to_null_value_Test extends AbstractTest_filter {

  @Test
  public void should_filter_iterable_elements_with_property_in_given_values() {
    rose.setTeam(null);
    durant.setTeam(null);
    Iterable<Player> filteredPlayers = filter(players).with("team").equalsTo(null).get();
    assertThat(filteredPlayers).containsOnly(rose, durant);
    // players is not modified
    assertThat(players).hasSize(4);
  }

}
