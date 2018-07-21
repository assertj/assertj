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
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.test.WithPlayerData;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.Test;

public class Filter_with_common_Test extends WithPlayerData {

  @Test
  public void filter_does_not_modify_given_iterable() {
    List<Player> playersCopy = newArrayList(players);
    // filter players
    filter(players).with("reboundsPerGame").equalsTo(5).get();
    assertThat(playersCopy).isEqualTo(players);
  }

  @Test
  public void should_fail_if_property_or_field_to_filter_on_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> filter(players).with("reboundsPerGame").equalsTo(5).and(null)
                                                                         .equalsTo("OKC"))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  public void should_fail_if_property_or_field_to_filter_on_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> filter(players).with("").equalsTo("OKC"))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

}