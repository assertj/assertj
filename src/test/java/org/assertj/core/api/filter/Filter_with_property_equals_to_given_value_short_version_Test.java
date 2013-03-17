package org.assertj.core.api.filter;

import org.assertj.core.test.Player;

import static org.assertj.core.api.filter.Filters.filter;


public class Filter_with_property_equals_to_given_value_short_version_Test extends AbstractTest_equals_filter {

  @Override
  protected Iterable<Player> filterIterable(Iterable<Player> employees, String propertyName, Object propertyValue) {
    return filter(employees).with(propertyName, propertyValue).get();
  }

}
