package org.assertj.core.api.filter;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import org.assertj.core.test.Player;
import org.assertj.core.util.introspection.IntrospectionError;

import org.junit.Test;

public class Filter_with_property_not_in_given_values_Test extends AbstractTest_filter {

  @Test
  public void should_filter_iterable_elements_with_property_not_in_given_values() {
    Iterable<Player> filteredPlayers = filter(players).with("team").notIn("OKC", "Miami Heat").get();
    assertThat(filteredPlayers).containsOnly(rose, noah);
    // players is not modified
    assertThat(players).hasSize(4);

    filteredPlayers = filter(players).with("name.last").notIn("Rose", "Noah").get();
    assertThat(filteredPlayers).containsOnly(durant, james);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_property_to_filter_on_is_null() {
    try {
      filter(players).with(null).notIn("foo", "bar");
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The property name to filter on should not be null", e.getMessage());
    }
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    try {
      filter(players).with("nickname").notIn("dude", "al");
      fail("IntrospectionError expected");
    } catch (IntrospectionError e) {
      assertEquals("No getter for property 'nickname' in org.assertj.core.test.Player", e.getMessage());
    }
  }

}