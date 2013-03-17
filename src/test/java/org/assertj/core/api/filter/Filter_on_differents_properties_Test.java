package org.assertj.core.api.filter;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;

import static org.junit.Assert.fail;

import org.assertj.core.util.IntrospectionError;
import org.junit.Test;


public class Filter_on_differents_properties_Test extends AbstractTest_filter {

  @Test
  public void should_filter_iterable_elements_on_different_properties() {
    // rose and durant have 5 rebounds per game but only rose does not play in OKC
    assertThat(filter(players).with("reboundsPerGame").equalsTo(5).and("team").notEqualsTo("OKC").get()).containsOnly(rose);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_property_to_filter_on_is_null() {
    try {
      filter(players).with("reboundsPerGame").equalsTo(5).and(null).equalsTo("OKC");
      fail("NullPointerException expected");
    } catch (NullPointerException e) {
      assertEquals("The property name to filter on should not be null", e.getMessage());
    }
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    try {
      filter(players).with("reboundsPerGame").equalsTo(5).and("nickname").notEqualsTo("dude");
      fail("IntrospectionError expected");
    } catch (IntrospectionError e) {
      assertEquals("No getter for property 'nickname' in org.assertj.core.test.Player", e.getMessage());
    }
  }

}