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
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ElementsShouldSatisfy.unsatisfiedRequirement;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.error.ElementsShouldSatisfy;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Maps_assertAnySatisfyingConsumer_Test extends MapsBaseTest {

  private Map<String, Player> greatPlayers;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    greatPlayers = mapOf(entry("Bulls", jordan), entry("Spurs", duncan), entry("Lakers", magic));
  }

  @Test
  public void should_pass_if_one_entry_satisfies_the_given_requirements() {
    maps.assertAnySatisfy(someInfo(), greatPlayers, (team, player) -> {
      assertThat(team).isEqualTo("Lakers");
      assertThat(player.getPointsPerGame()).isGreaterThan(18);
    });
  }

  @Test
  public void should_fail_if_the_map_under_test_is_empty_whatever_the_assertions_requirements_are() {
    // GIVEN
    actual.clear();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAnySatisfy(someInfo(), actual,
                                                                            ($1, $2) -> assertThat(true).isTrue()));
    // THEN
    assertThat(error).hasMessage(elementsShouldSatisfyAny(actual, emptyList(), someInfo()).create());
  }

  @Test
  public void should_fail_if_no_entry_satisfies_the_given_requirements() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAnySatisfy(someInfo(), actual,
                                                                            ($1, $2) -> assertThat(true).isFalse()));
    // THEN
    Iterator<Entry<String, String>> actualEntries = actual.entrySet().iterator();
    List<ElementsShouldSatisfy.UnsatisfiedRequirement> errors = list(unsatisfiedRequirement(actualEntries.next(),
                                                                                            format("%n" +
                                                                                                   "Expecting:%n" +
                                                                                                   " <true>%n" +
                                                                                                   "to be equal to:%n" +
                                                                                                   " <false>%n" +
                                                                                                   "but was not.")),
                                                                     unsatisfiedRequirement(actualEntries.next(),
                                                                                            format("%n" +
                                                                                                   "Expecting:%n" +
                                                                                                   " <true>%n" +
                                                                                                   "to be equal to:%n" +
                                                                                                   " <false>%n" +
                                                                                                   "but was not.")));
    assertThat(error).hasMessage(elementsShouldSatisfyAny(actual, errors, someInfo()).create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertAnySatisfy(someInfo(), null, (team, player) -> {}));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_given_requirements_are_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertAnySatisfy(someInfo(), greatPlayers, null))
                                    .withMessage("The BiConsumer<K, V> expressing the assertions requirements must not be null");
  }
}
