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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Maps_assertNoneSatisfy_Test extends MapsBaseTest {

  private Map<String, Player> greatPlayers;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    greatPlayers = mapOf(entry("Bulls", jordan), entry("Spurs", duncan), entry("Lakers", magic));
  }

  @Test
  void should_pass_if_no_entries_satisfy_the_given_requirements() {
    maps.assertNoneSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Spurs");
      assertThat(player.getPointsPerGame()).isGreaterThan(20);
    });
  }

  @Test
  void should_pass_if_actual_map_is_empty() {
    // GIVEN
    greatPlayers.clear();
    // THEN
    maps.assertNoneSatisfy(someInfo(), greatPlayers, ($1, $2) -> assertThat(true).isFalse());
  }

  @Test
  void should_fail_if_one_entry_satisfies_the_given_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertNoneSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Lakers", "Bulls");
      assertThat(player.getPointsPerGame()).as("%s %s ppg", player.getName().first, player.getName().getLast())
                                           .isLessThan(30);
    }));
    // THEN
    List<Map.Entry<?, ?>> erroneousEntries = list(createEntry("Lakers", magic));
    assertThat(error).hasMessage(noElementsShouldSatisfy(greatPlayers, erroneousEntries).create());
  }

  @Test
  void should_fail_if_multiple_entries_satisfy_the_given_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertNoneSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Lakers", "Bulls", "Spurs");
      assertThat(player.getPointsPerGame()).as("%s %s ppg", player.getName().first, player.getName().getLast())
                                           .isLessThan(30);
    }));
    // THEN
    List<Map.Entry<?, ?>> erroneousEntries = list(createEntry("Spurs", duncan),
                                                  createEntry("Lakers", magic));
    assertThat(error).hasMessage(noElementsShouldSatisfy(greatPlayers, erroneousEntries).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertNoneSatisfy(someInfo(), null, (team, player) -> {}));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_requirements_are_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertNoneSatisfy(someInfo(), greatPlayers, null))
                                    .withMessage("The BiConsumer<K, V> expressing the assertions requirements must not be null");
  }

  private static Map.Entry<String, Player> createEntry(String team, Player player) {
    return new AbstractMap.SimpleEntry<>(team, player);
  }
}
