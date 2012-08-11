package org.fest.assertions.api.filter;

import static org.fest.assertions.api.filter.Filters.filter;

import org.fest.test.Player;

public class Filter_with_property_equals_to_given_value_short_version_Test extends AbstractTest_equals_filter {

  @Override
  protected Iterable<Player> filterIterable(Iterable<Player> employees, String propertyName, Object propertyValue) {
    return filter(employees).with(propertyName, propertyValue).get();
  }

}
