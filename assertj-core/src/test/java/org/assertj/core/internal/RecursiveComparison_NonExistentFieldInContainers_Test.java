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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for AssertJ issue #3354.
 * <p>
 * Verifies that comparing a non-existent field within a container (Iterable, Array, Optional, AtomicReference)
 * correctly throws an {@link IllegalArgumentException}.
 *
 * @author Dongmin Cha
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class RecursiveComparison_NonExistentFieldInContainers_Test {

  static class Player {
    String name;

    Player(String name) {
      this.name = name;
    }
  }

  static class TeamWithList {
    List<Player> players;

    TeamWithList(List<Player> players) {
      this.players = players;
    }
  }

  static class TeamWithSet {
    Set<Player> players;

    TeamWithSet(Set<Player> players) {
      this.players = players;
    }
  }

  static class TeamWithArray {
    Player[] players;

    TeamWithArray(Player[] players) {
      this.players = players;
    }
  }

  static class TeamWithOptionalPlayer {
    Optional<Player> player;

    TeamWithOptionalPlayer(Optional<Player> player) {
      this.player = player;
    }
  }

  static class TeamWithAtomicReferencePlayer {
    AtomicReference<Player> player;

    TeamWithAtomicReferencePlayer(AtomicReference<Player> player) {
      this.player = player;
    }
  }

  @Nested
  @DisplayName("for Iterable containers (List/Set)")
  class ForIterable {

    @Test
    void should_throw_exception_when_comparing_non_existent_field_in_list() {
      // GIVEN
      var actual = new TeamWithList(List.of(new Player("Son")));
      var expected = new TeamWithList(List.of(new Player("Maddison")));
      // WHEN & THEN
      thenIllegalArgumentException().isThrownBy(() -> assertThat(actual).usingRecursiveComparison()
                                                                        .comparingOnlyFields("players.salary")
                                                                        .isEqualTo(expected))
                                    .withMessageContaining("players.salary");
    }

    @Test
    void should_throw_exception_when_comparing_non_existent_field_in_set() {
      // GIVEN
      var actual = new TeamWithSet(Set.of(new Player("Son")));
      var expected = new TeamWithSet(Set.of(new Player("Maddison")));
      // WHEN & THEN
      thenIllegalArgumentException().isThrownBy(() -> assertThat(actual).usingRecursiveComparison()
                                                                        .comparingOnlyFields("players.salary")
                                                                        .isEqualTo(expected))
                                    .withMessageContaining("players.salary");
    }
  }

  @Nested
  @DisplayName("for Array containers")
  class ForArray {

    @Test
    void should_throw_exception_when_comparing_non_existent_field_in_array() {
      // GIVEN
      var actual = new TeamWithArray(new Player[] { new Player("Son") });
      var expected = new TeamWithArray(new Player[] { new Player("Maddison") });
      // WHEN & THEN
      thenIllegalArgumentException().isThrownBy(() -> assertThat(actual).usingRecursiveComparison()
                                                                        .comparingOnlyFields("players.salary")
                                                                        .isEqualTo(expected))
                                    .withMessageContaining("players.salary");
    }
  }

  @Nested
  @DisplayName("for Optional wrappers")
  class ForOptional {

    @Test
    void should_throw_exception_when_comparing_non_existent_field_in_optional() {
      // GIVEN
      var actual = new TeamWithOptionalPlayer(Optional.of(new Player("Son")));
      var expected = new TeamWithOptionalPlayer(Optional.of(new Player("Kane")));
      // WHEN & THEN
      thenIllegalArgumentException().isThrownBy(() -> assertThat(actual).usingRecursiveComparison()
                                                                        .comparingOnlyFields("player.salary")
                                                                        .isEqualTo(expected))
                                    .withMessageContaining("player.salary");
    }
  }

  @Nested
  @DisplayName("for AtomicReference wrappers")
  class ForAtomicReference {

    @Test
    void should_throw_exception_when_comparing_non_existent_field_in_atomic_reference() {
      // GIVEN
      var actual = new TeamWithAtomicReferencePlayer(new AtomicReference<>(new Player("Son")));
      var expected = new TeamWithAtomicReferencePlayer(new AtomicReference<>(new Player("Kane")));
      // WHEN & THEN
      thenIllegalArgumentException().isThrownBy(() -> assertThat(actual).usingRecursiveComparison()
                                                                        .comparingOnlyFields("player.salary")
                                                                        .isEqualTo(expected))
                                    .withMessageContaining("player.salary");
    }
  }
}
