/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.maps;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.function.BiConsumer;

import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.testkit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Maps_assertAnySatisfyingConsumer_Test extends MapsBaseTest {

  private Map<String, Player> greatPlayers;

  @Mock
  private BiConsumer<String, Player> consumer;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    greatPlayers = mapOf(entry("Bulls", jordan), entry("Spurs", duncan), entry("Lakers", magic));
  }

  @Test
  void must_not_check_all_entries() {
    // GIVEN
    assertThat(greatPlayers).hasSizeGreaterThan(2); // This test requires a map with size > 2
    // first entry does not match -> assertion error, 2nd entry does match -> doNothing()
    doThrow(new AssertionError("some error message")).doNothing().when(consumer).accept(anyString(), any(Player.class));
    // WHEN
    maps.assertAnySatisfy(INFO, greatPlayers, consumer);
    // THEN
    // make sure that we only evaluated 2 out of 3 entries
    verify(consumer, times(2)).accept(anyString(), any(Player.class));
  }

  @Test
  void should_pass_if_one_entry_satisfies_the_given_requirements() {
    maps.assertAnySatisfy(INFO, greatPlayers, (team, player) -> {
      assertThat(team).isEqualTo("Lakers");
      assertThat(player.getPointsPerGame()).isGreaterThan(18);
    });
  }

  @Test
  void should_fail_if_the_map_under_test_is_empty_whatever_the_assertions_requirements_are() {
    // GIVEN
    actual.clear();
    // WHEN
    var error = expectAssertionError(() -> maps.assertAnySatisfy(INFO, actual, ($1, $2) -> assertThat(true).isTrue()));
    // THEN
    then(error).hasMessage(elementsShouldSatisfyAny(actual, emptyList(), INFO).create());
  }

  @Test
  void should_fail_if_no_entry_satisfies_the_given_requirements() {
    // WHEN
    BiConsumer<String, String> requirements = ($1, $2) -> assertThat(true).isFalse();
    var error = expectAssertionError(() -> maps.assertAnySatisfy(INFO, actual, requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(error).hasMessageStartingWith(format("%n" +
                                              "Expecting any element of:%n" +
                                              "  %s%n" +
                                              "to satisfy the given assertions requirements but none did:%n%n",
                                              info.representation().toStringOf(actual)));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> maps.assertAnySatisfy(INFO, null, (team, player) -> {}));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_requirements_are_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertAnySatisfy(INFO, greatPlayers, null))
                                    .withMessage("The BiConsumer<K, V> expressing the assertions requirements must not be null");
  }

}
