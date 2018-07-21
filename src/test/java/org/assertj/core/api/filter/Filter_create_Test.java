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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.filter.Filters.filter;

import java.util.List;

import org.assertj.core.test.Player;
import org.assertj.core.test.WithPlayerData;
import org.junit.jupiter.api.Test;


public class Filter_create_Test extends WithPlayerData {

  @Test
  public void should_create_filter_from_iterable() {
    Filters<Player> filter = filter(players);
    assertThat(filter.get()).isEqualTo(players);
  }

  @Test
  public void should_create_filter_from_array() {
    Player[] playersArray = players.toArray(new Player[0]);
    Filters<Player> filter = filter(playersArray);
    assertThat(filter.get()).isEqualTo(players);
  }

  @Test
  public void should_fail_if_constructor_iterable_parameter_is_null() {
    assertThatNullPointerException().isThrownBy(() -> filter((List<Player>) null))
                                    .withMessage("The iterable to filter should not be null");
  }

  @Test
  public void should_fail_if_constructor_array_parameter_is_null() {
    assertThatNullPointerException().isThrownBy(() -> filter((Player[]) null))
                                    .withMessage("The array to filter should not be null");
  }

}
