package org.fest.assertions.api.filter;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.test.Player;

public class Filter_create_Test extends AbstractTest_filter {

  @Test
  public void should_create_filter_from_iterable() {
    Filters<Player> filter = filter(players);
    assertEquals(players, filter.get());
  }

  @Test
  public void should_create_filter_from_array() {
    Player[] playersArray = players.toArray(new Player[0]);
    Filters<Player> filter = filter(playersArray);
    assertEquals(players, filter.get());
  }
  
  @Test
  public void should_fail_if_constructor_iterable_parameter_is_null() {
    try {
      filter((List<Player>)null);
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The iterable to filter should not be null", e.getMessage());
    }
  }

  @Test
  public void should_fail_if_constructor_array_parameter_is_null() {
    try {
      filter((Player[])null);
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The array to filter should not be null", e.getMessage());
    }
  }
  
}
