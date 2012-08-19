package org.fest.assertions.api.filter;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import org.junit.Test;

import org.fest.test.Player;
import org.fest.util.IntrospectionError;

public abstract class AbstractTest_equals_filter extends AbstractTest_filter {

  @Test
  public void should_filter_iterable_elements_with_property_equals_to_given_value() {
    Iterable<Player> bullsPlayers = filterIterable(players, "team", "Chicago Bulls");
    assertThat(bullsPlayers).containsOnly(rose, noah);
    // players is not modified
    assertThat(players).hasSize(4);

    Iterable<Player> filteredPlayers = filter(players).with("name.last", "James").get();
    assertThat(filteredPlayers).containsOnly(james);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  protected abstract Iterable<Player> filterIterable(Iterable<Player> players, String propertyName, Object propertyValue);

  @Test
  public void should_fail_if_property_to_filter_on_is_null() {
    try {
      filterIterable(players, null, 6000L);
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("The property name to filter on should not be null");
    }
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    try {
      filterIterable(players, "nickname", "dude");
      fail("IntrospectionError expected");
    } catch (IntrospectionError e) {
      assertThat(e).hasMessage("No getter for property 'nickname' in org.fest.test.Player");
    }
  }

}