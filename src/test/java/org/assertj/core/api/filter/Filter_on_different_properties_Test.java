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
import static org.assertj.core.api.filter.Filters.filter;

import org.assertj.core.test.Player;
import org.assertj.core.test.WithPlayerData;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

public class Filter_on_different_properties_Test extends WithPlayerData {

  @Test
  public void should_filter_iterable_elements_on_different_properties() {
    // rose and durant have 5 rebounds per game but only rose does not play in OKC
    Iterable<Player> filteredPlayers = filter(players).with("reboundsPerGame").equalsTo(5)
                                                      .and("team").notEqualsTo("Chicago Bulls").get();
    assertThat(filteredPlayers).containsOnly(kobe);
  }

  @Test
  public void should_fail_if_elements_to_filter_do_not_have_one_of_the_property_or_field_used_by_filter() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> filter(players).with("reboundsPerGame")
                                                                                        .equalsTo(5)
                                                                                        .and("numberOfTitle")
                                                                                        .notEqualsTo(0))
                                                       .withMessageContaining("Can't find any field or property with name 'numberOfTitle'");
  }

}