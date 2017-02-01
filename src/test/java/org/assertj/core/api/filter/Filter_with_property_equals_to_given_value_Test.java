/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.filter;

import org.assertj.core.test.Player;

import static org.assertj.core.api.filter.Filters.filter;


public class Filter_with_property_equals_to_given_value_Test extends AbstractTest_equals_filter {

  @Override
  protected Iterable<Player> filterIterable(Iterable<Player> employees, String propertyName, Object propertyValue) {
    return filter(employees).with(propertyName).equalsTo(propertyValue).get();
  }

}
