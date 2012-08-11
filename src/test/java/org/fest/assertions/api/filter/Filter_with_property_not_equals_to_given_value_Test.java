package org.fest.assertions.api.filter;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import org.junit.Test;

import org.fest.test.Player;
import org.fest.util.IntrospectionError;

public class Filter_with_property_not_equals_to_given_value_Test extends AbstractTest_filter {

  @Test
  public void should_filter_iterable_elements_with_property_not_equals_to_given_value() {
    Iterable<Player> nonOKCPlayers = filter(players).with("team").notEqualsTo("OKC").get();
    assertThat(nonOKCPlayers).containsOnly(rose, noah, james);
    // players is not modified
    assertThat(players).hasSize(4);

    Iterable<Player> filteredPlayers = filter(players).with("name.last").notEqualsTo("Rose").get();
    assertThat(filteredPlayers).containsOnly(durant, noah, james);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_property_to_filter_on_is_null() {
    try {
      filter(players).with(null).notEqualsTo("foo");
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The property name to filter on should not be null", e.getMessage());
    }
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    try {
      filter(players).with("nickname").notEqualsTo("dude");
      fail("IntrospectionError expected");
    } catch (IntrospectionError e) {
      assertEquals("No getter for property 'nickname' in org.fest.test.Player", e.getMessage());
    }
  }

}