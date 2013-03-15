package org.assert4j.core.assertions.api.filter;

import org.assert4j.core.test.Player;

import static org.assert4j.core.assertions.api.filter.Filters.filter;


public class Filter_with_property_equals_to_given_value_Test extends AbstractTest_equals_filter {

  @Override
  protected Iterable<Player> filterIterable(Iterable<Player> employees, String propertyName, Object propertyValue) {
    return filter(employees).with(propertyName).equalsTo(propertyValue).get();
  }

}
