package org.fest.assertions.api.filter;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.filter.Filters.filter;
import static org.fest.util.Collections.sizeOf;

import static org.junit.Assert.fail;

import org.junit.Test;

import org.fest.assertions.test.Player;

public class Filter_being_condition_Test extends AbstractTest_filter {

  private PotentialMvpCondition potentialMvp= new PotentialMvpCondition();
  
  @Test
  public void should_filter_iterable_elements_satisfying_condition() {
    Iterable<Player> potentialMvpPlayers = filter(players).being(potentialMvp).get();
    assertThat(potentialMvpPlayers).containsOnly(rose, james);
    // players is not modified
    assertEquals(4, sizeOf(players));
  }

  @Test
  public void should_fail_if_filter_condition_is_null() {
    try {
      filter(players).being(null);
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The filter condition should not be null", e.getMessage());
    }
  }

}