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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Player;
import org.junit.Rule;
import org.junit.Test;


public class Filter_with_property_not_equals_to_given_value_Test extends AbstractTest_filter {

  @Rule
  public ExpectedException thrown = none();

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
    thrown.expectIllegalArgumentException("The property/field name to filter on should not be null or empty");
    filter(players).with(null).notEqualsTo("foo");
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    thrown.expectIntrospectionErrorWithMessageContaining("Can't find any field or property with name 'country'");
    filter(players).with("country").notEqualsTo("France");
  }

}