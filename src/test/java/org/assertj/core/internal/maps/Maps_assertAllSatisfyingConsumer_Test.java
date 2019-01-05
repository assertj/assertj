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
package org.assertj.core.internal.maps;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import org.assertj.core.error.ElementsShouldSatisfy.UnsatisfiedRequirement;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Maps_assertAllSatisfyingConsumer_Test extends MapsBaseTest {

  private Map<String, Player> greatPlayers;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    greatPlayers = mapOf(entry("Bulls", jordan), entry("Spurs", duncan), entry("Lakers", magic));
  }

  @Test
  public void should_pass_if_all_entries_satisfy_the_given_requirements() {
    maps.assertAllSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Lakers", "Bulls", "Spurs");
      assertThat(player.getPointsPerGame()).isGreaterThan(18);
    });
  }

  @Test
  public void should_pass_if_actual_map_is_empty() {
    // GIVEN
    greatPlayers.clear();

    // WHEN THEN
    maps.assertAllSatisfy(someInfo(), greatPlayers, ($1, $2) -> {
      assertThat(true).isFalse();
    });
  }

  @Test
  public void should_fail_if_one_entry_does_not_satisfy_the_given_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAllSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Lakers", "Bulls", "Spurs");
      assertThat(player.getPointsPerGame()).as("%s %s ppg", player.getName().first, player.getName().getLast())
                                           .isLessThan(30);
    }));

    // THEN
    assertThat(error).hasMessage(elementsShouldSatisfy(greatPlayers, list(failOnPpgLessThan("Bulls", jordan, 30))).create());
  }

  @Test
  public void should_report_all_the_entries_not_satisfying_the_given_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAllSatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isIn("Lakers", "Bulls", "Spurs");
      assertThat(player.getPointsPerGame()).as("%s %s ppg", player.getName().first, player.getName().getLast())
                                           .isGreaterThanOrEqualTo(30);
    }));

    // THEN
    assertThat(error).hasMessage(elementsShouldSatisfy(greatPlayers,
                                                       list(failOnPpgGreaterThanEqual("Spurs", duncan, 30),
                                                            failOnPpgGreaterThanEqual("Lakers", magic, 30))).create());
  }

  private UnsatisfiedRequirement failOnPpgGreaterThanEqual(String team, Player player, int requiredScore) {
    SimpleEntry<String, Player> entry = new AbstractMap.SimpleEntry<>(team, player);
    String message = format("[" + player.getName().getName() + " ppg] %n" +
                            "Expecting:%n" +
                            " <" + player.getPointsPerGame() + ">%n" +
                            "to be greater than or equal to:%n" +
                            " <" + requiredScore + "> ");
    return new UnsatisfiedRequirement(entry, message);
  }

  private UnsatisfiedRequirement failOnPpgLessThan(String team, Player player, int requiredScore) {
    SimpleEntry<String, Player> entry = new AbstractMap.SimpleEntry<>(team, player);
    String message = format("[" + player.getName().getName() + " ppg] %n" +
                            "Expecting:%n" +
                            " <" + player.getPointsPerGame() + ">%n" +
                            "to be less than:%n" +
                            " <" + requiredScore + "> ");
    return new UnsatisfiedRequirement(entry, message);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAllSatisfy(someInfo(), null, (team, player) -> {}));

    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_given_requirements_are_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertAllSatisfy(someInfo(), greatPlayers, null))
                                    .withMessage("The BiConsumer<K, V> expressing the assertions requirements must not be null");
  }
}
