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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.filter.Filters.filter;

import org.assertj.core.test.Player;
import org.assertj.core.test.PotentialMvpCondition;
import org.assertj.core.test.WithPlayerData;
import org.junit.jupiter.api.Test;


public class Filter_having_condition_Test extends WithPlayerData {

  private PotentialMvpCondition mvpStats = new PotentialMvpCondition();

  @Test
  public void should_filter_iterable_elements_satisfying_condition() {
    Iterable<Player> playersHavinMvpStats = filter(players).having(mvpStats).get();
    assertThat(playersHavinMvpStats).containsOnly(jordan);
    // players is not modified
    assertThat(players).hasSize(4);
  }

  @Test
  public void should_fail_if_filter_condition_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> filter(players).having(null))
                                        .withMessage("The filter condition should not be null");
  }

}