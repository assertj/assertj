/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.filter.Filters.filter;

import org.assertj.core.test.Player;
import org.assertj.core.test.WithPlayerData;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;


public class Filter_with_property_not_equals_to_given_value_Test extends WithPlayerData {

  @Test
  public void should_filter_iterable_elements_with_property_not_equals_to_given_value() {
    Iterable<Player> nonOKCPlayers = filter(players).with("team").notEqualsTo("Chicago Bulls").get();
    assertThat(nonOKCPlayers).containsOnly(kobe, duncan, magic);
    // players is not modified
    assertThat(players).hasSize(4);

    Iterable<Player> filteredPlayers = filter(players).with("name.last").notEqualsTo("Jordan").get();
    assertThat(filteredPlayers).containsOnly(kobe, duncan, magic);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_property_to_filter_on_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> filter(players).with(null).notEqualsTo("foo"))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_property_used_by_filter() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> filter(players).with("country")
                                                                                        .notEqualsTo("France"))
                                                       .withMessageContaining("Can't find any field or property with name 'country'");
  }

}