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
package org.assertj.core.internal.maps;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Map;

import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Player;
import org.junit.Before;
import org.junit.Test;

public class Maps_assertAllSatisfyingConsumer_Test extends MapsBaseTest {

  private Map<String, Player> greatPlayers;

  @Override
  @Before
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
    greatPlayers.clear();
    maps.assertAllSatisfy(someInfo(), greatPlayers, (team, player) -> {
      // whatever, this is never called
      assertThat(player.getPointsPerGame()).isGreaterThan(200);
    });
  }

  @Test
  public void should_fail_if_one_entry_does_not_satisfy_the_given_requirements() {
    try {
      maps.assertAllSatisfy(someInfo(), greatPlayers, (team, player) -> {
        assertThat(team).isIn("Lakers", "Bulls", "Spurs");
        assertThat(player.getPointsPerGame()).as("%s %s ppg", player.getName().first, player.getName().getLast())
                                             .isGreaterThanOrEqualTo(30);
      });
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("[Tim Duncan ppg] %n" +
                                      "Expecting:%n" +
                                      " <19>%n" +
                                      "to be greater than or equal to:%n" +
                                      " <30> "));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertAllSatisfy(someInfo(), null, (team, player) -> {});
  }

  @Test
  public void should_fail_if_given_requirements_are_null() {
    thrown.expectNullPointerException("The BiConsumer<K, V> expressing the assertions requirements must not be null");
    maps.assertAllSatisfy(someInfo(), greatPlayers, null);
  }
}
