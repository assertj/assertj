package org.fest.assertions.api.filter;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import org.junit.Test;

import org.fest.test.Player;

public class Filter_being_condition_Test extends AbstractTest_filter {

  private PotentialMvpCondition potentialMvp = new PotentialMvpCondition();

  @Test
  public void should_filter_iterable_elements_satisfying_condition() {
    Iterable<Player> potentialMvpPlayers = filter(players).being(potentialMvp).get();
    assertThat(potentialMvpPlayers).containsOnly(rose, james);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_filter_condition_is_null() {
    try {
      filter(players).being(null);
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("The filter condition should not be null");
    }
  }

}