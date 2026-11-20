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
package example.core;

// @formatter:off

import static example.data.Race.DWARF;
import static example.data.Race.ELF;
import static example.data.Race.HOBBIT;
import static example.data.Race.MAIA;
import static example.data.Race.MAN;
import static example.data.Race.ORC;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
// tag::filter_imports[]
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.notIn;
// end::filter_imports[]
// tag::tuple_import[]
// when checking several properties/fields you have to use tuples:
import static org.assertj.core.api.Assertions.tuple;
// end::tuple_import[]
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.List;

// tag::condition_import[]
import org.assertj.core.api.Condition;
// end::condition_import[]
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

// not run by the build (class name does not match *Test)
class GroupAssertionsExample {

  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
  private final TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
  private final TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
  private final TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
  private final TolkienCharacter gandalf = new TolkienCharacter("Gandalf", 2020, MAIA);
  private final TolkienCharacter legolas = new TolkienCharacter("Legolas", 1000, ELF);
  private final TolkienCharacter boromir = new TolkienCharacter("Boromir", 37, MAN);
  private final TolkienCharacter aragorn = new TolkienCharacter("Aragorn", 87, MAN);
  private final TolkienCharacter gimli = new TolkienCharacter("Gimli", 139, DWARF);
  private final TolkienCharacter sauron = new TolkienCharacter("Sauron", 50000, MAIA);

  private final List<TolkienCharacter> fellowshipOfTheRing = list(frodo, sam, merry, pippin, gandalf, legolas, boromir,
                                                                  aragorn, gimli);

  private final TolkienDao tolkienDao = () -> fellowshipOfTheRing;

  @Test
  void satisfy() {
    // tag::satisfy[]
    List<TolkienCharacter> hobbits = list(frodo, sam, pippin);

    // all elements must satisfy the given assertions
    assertThat(hobbits).allSatisfy(character -> {
      assertThat(character.getRace()).isEqualTo(HOBBIT);
      assertThat(character.getName()).isNotEqualTo("Sauron");
    });

    // at least one element must satisfy the given assertions
    assertThat(hobbits).anySatisfy(character -> {
      assertThat(character.getRace()).isEqualTo(HOBBIT);
      assertThat(character.getName()).isEqualTo("Sam");
    });

    // no element must satisfy the given assertions
    assertThat(hobbits).noneSatisfy(character -> assertThat(character.getRace()).isEqualTo(ELF));
    // end::satisfy[]
  }

  @Test
  void match() {
    // tag::match[]
    List<TolkienCharacter> hobbits = list(frodo, sam, pippin);

    assertThat(hobbits).allMatch(character -> character.getRace() == HOBBIT, "hobbits")
                       .anyMatch(character -> character.getName().contains("pp"))
                       .noneMatch(character -> character.getRace() == ORC);
    // end::match[]
  }

  @Test
  void navigation() {
    // tag::navigation[]
    // only object assertions available after navigation
    Iterable<TolkienCharacter> hobbits = list(frodo, sam, pippin);
    assertThat(hobbits).first().isEqualTo(frodo);
    assertThat(hobbits).element(1).isEqualTo(sam);
    assertThat(hobbits).last().isEqualTo(pippin);

    // strongly typed String assertions after navigation
    Iterable<String> hobbitsName = list("frodo", "sam", "pippin");
    // STRING is an InstanceOfAssertFactory from org.assertj.core.api.InstanceOfAssertFactories.STRING
    // as() is just synthetic sugar for readability
    assertThat(hobbitsName).first(as(STRING))
                           .startsWith("fro")
                           .endsWith("do");
    assertThat(hobbitsName).element(1, as(STRING))
                           .startsWith("sa")
                           .endsWith("am");
    assertThat(hobbitsName).last(as(STRING))
                           .startsWith("pip")
                           .endsWith("pin");

    // alternative for strongly typed assertions
    assertThat(hobbitsName, StringAssert.class).first()
                                               .startsWith("fro")
                                               .endsWith("do");
    // end::navigation[]
  }

  @Test
  void single_element() {
    // tag::single_element[]
    Iterable<String> babySimpsons = list("Maggie");

    // only object assertions available
    assertThat(babySimpsons).singleElement()
                            .isEqualTo("Maggie");

    // to get specific typed assertions, pass the corresponding InstanceOfAssertFactory from
    // org.assertj.core.api.InstanceOfAssertFactories.STRING), as() is just synthetic sugar for readability
    assertThat(babySimpsons).singleElement(as(STRING))
                            .endsWith("gie");

    // alternative for strongly typed assertions
    assertThat(babySimpsons, StringAssert.class).singleElement()
                                                .startsWith("Mag");
    // end::single_element[]
  }

  @Test
  void filtered_on_predicate() {
    // tag::filtered_on_predicate[]
    assertThat(fellowshipOfTheRing).filteredOn( character -> character.getName().contains("o") )
                                   .containsOnly(aragorn, frodo, legolas, boromir);
    // end::filtered_on_predicate[]
  }

  @Test
  void filtered_on_property() {
    // tag::filtered_on_property[]
    // filters use introspection to get property/field values
    assertThat(fellowshipOfTheRing).filteredOn("race", HOBBIT)
                                   .containsOnly(sam, frodo, pippin, merry);

    // nested properties are supported
    assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man")
                                   .containsOnly(aragorn, boromir);

    // you can apply different comparison
    assertThat(fellowshipOfTheRing).filteredOn("race", notIn(HOBBIT, MAN))
                                   .containsOnly(gandalf, gimli, legolas);

    assertThat(fellowshipOfTheRing).filteredOn("race", in(MAIA, MAN))
                                   .containsOnly(gandalf, boromir, aragorn);

    assertThat(fellowshipOfTheRing).filteredOn("race", not(HOBBIT))
                                   .containsOnly(gandalf, boromir, aragorn, gimli, legolas);

    // you can chain multiple filter criteria
    assertThat(fellowshipOfTheRing).filteredOn("race", MAN)
                                   .filteredOn("name", not("Boromir"))
                                   .containsOnly(aragorn);
    // end::filtered_on_property[]
  }

  @Test
  void filtered_on_function() {
    // tag::filtered_on_function[]
    assertThat(fellowshipOfTheRing).filteredOn(TolkienCharacter::getRace, HOBBIT)
                                   .containsOnly(sam, frodo, pippin, merry);
    // end::filtered_on_function[]
  }

  @Test
  void filtered_on_null() {
    // tag::filtered_on_null[]
    TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
    TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
    TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
    TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 38, HOBBIT);

    List<TolkienCharacter> hobbits = list(frodo, mysteriousHobbit, merry, pippin);

    assertThat(hobbits).filteredOnNull("name")
                       .singleElement()
                       .isEqualTo(mysteriousHobbit);
    // end::filtered_on_null[]
  }

  @Test
  void filtered_on_assertions() {
    // tag::filtered_on_assertions[]
    TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
    TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
    TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
    TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);

    List<TolkienCharacter> hobbits = list(frodo, sam, merry, pippin);

    assertThat(hobbits).filteredOnAssertions(hobbit -> assertThat(hobbit.age).isLessThan(34))
                       .containsOnly(frodo, pippin);
    // end::filtered_on_assertions[]
  }

  @Test
  void filtered_on_condition() {
    Player rose = new Player(25, 8, 5);
    Player lebron = new Player(27, 6, 9);
    Player noah = new Player(8, 5, 11);

    // tag::filtered_on_condition[]
    Condition<Player> mvpStats = new Condition<Player>(player -> {
        return player.pointsPerGame() > 20 && (player.assistsPerGame() >= 8 || player.reboundsPerGame() >= 8);
      }, "mvp");

    List<Player> players = new ArrayList<>();
    players.add(rose); // Derrick Rose: 25 ppg - 8 assists - 5 rebounds
    players.add(lebron); // Lebron James: 27 ppg - 6 assists - 9 rebounds
    players.add(noah); // Joachim Noah: 8 ppg - 5 assists - 11 rebounds

    // noah does not have more than 20 ppg
    assertThat(players).filteredOn(mvpStats)
                       .containsOnly(rose, lebron);
    // end::filtered_on_condition[]
  }

  @Test
  void without_extracting() {
    // tag::without_extracting[]
    List<TolkienCharacter> fellowshipOfTheRing = tolkienDao.findHeroes();  // frodo, sam, aragorn ...

    // requires creation of frodo and aragorn, the expected TolkienCharacters
    assertThat(fellowshipOfTheRing).contains(frodo, aragorn);
    // end::without_extracting[]
  }

  @Test
  void extracting_with_stream() {
    // tag::extracting_with_stream[]
    // extract the names ...
    List<String> names = fellowshipOfTheRing.stream().map(TolkienCharacter::getName).collect(toList());
    // ... and finally assert something
    assertThat(names).contains("Boromir", "Gandalf", "Frodo", "Legolas");
    // end::extracting_with_stream[]
  }

  @Test
  void extracting_single_value() {
    // tag::extracting_single_value[]
    // "name" needs to be either a property or a field of the TolkienCharacter class
    assertThat(fellowshipOfTheRing).extracting("name")
                                   .contains("Boromir", "Gandalf", "Frodo", "Legolas")
                                   .doesNotContain("Sauron", "Elrond");

    // specifying nested field/property is supported
    assertThat(fellowshipOfTheRing).extracting("race.name")
                                   .contains("Man", "Maia", "Hobbit", "Elf");

    // same thing with a lambda which is type safe and refactoring friendly:
    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName)
                                   .contains("Boromir", "Gandalf", "Frodo", "Legolas");

    // same thing map an alias of extracting:
    assertThat(fellowshipOfTheRing).map(TolkienCharacter::getName)
                                   .contains("Boromir", "Gandalf", "Frodo", "Legolas");
    // end::extracting_single_value[]
  }

  @Test
  void extracting_single_typed_value() {
    // tag::extracting_single_typed_value[]
    // to have type safe extracting, use the second parameter to pass the expected property type:
    assertThat(fellowshipOfTheRing).extracting("name", String.class)
                                   .contains("Boromir", "Gandalf", "Frodo", "Legolas")
                                   .doesNotContain("Sauron", "Elrond");
    // end::extracting_single_typed_value[]
  }

  @Test
  void extracting_multiple_values() {
    // tag::extracting_multiple_values[]
    // extracting name, age and race.name nested property
    assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                                   .contains(tuple("Boromir", 37, "Man"),
                                             tuple("Sam", 38, "Hobbit"),
                                             tuple("Legolas", 1000, "Elf"));

    // same assertion with functions for type safety:
    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               tolkienCharacter -> tolkienCharacter.age,
                                               tolkienCharacter -> tolkienCharacter.getRace().getName())
                                   .contains(tuple("Boromir", 37, "Man"),
                                             tuple("Sam", 38, "Hobbit"),
                                             tuple("Legolas", 1000, "Elf"));
    // end::extracting_multiple_values[]
  }

  @Test
  void flat_extracting() {
    Player pippen = new Player(16, 5, 6);
    Player kukoc = new Player(12, 4, 4);
    Player jabbar = new Player(25, 4, 11);
    Player worthy = new Player(18, 3, 5);
    Player jordan = new Player(30, 5, 6, pippen, kukoc);
    Player magic = new Player(20, 11, 7, jabbar, worthy);
    // tag::flat_extracting[]
    List<Player> reallyGoodPlayers = list(jordan, magic);

    // check all team mates by specifying the teamMates property (Player has a getTeamMates() method):
    assertThat(reallyGoodPlayers).flatExtracting("teamMates")
                                 .contains(pippen, kukoc, jabbar, worthy);

    // alternatively, you can use a Function for type safety:
    assertThat(reallyGoodPlayers).flatExtracting(Player::getTeamMates)
                                 .contains(pippen, kukoc, jabbar, worthy);

    // flatMap is an alias of flatExtracting:
    assertThat(reallyGoodPlayers).flatMap(Player::getTeamMates)
                                 .contains(pippen, kukoc, jabbar, worthy);

    // if you use extracting instead of flatExtracting the result would be a list of list of players so the assertion becomes:
    assertThat(reallyGoodPlayers).extracting("teamMates")
                                 .contains(list(pippen, kukoc), list(jabbar, worthy));
    // end::flat_extracting[]
  }

  @Test
  void flat_extracting_multiple_values() {
    // tag::flat_extracting_multiple_values[]
    // extract a list of values, flatten them and use contains assertion
    assertThat(fellowshipOfTheRing).flatExtracting("name", "race.name")
                                   .contains("Frodo", "Hobbit", "Legolas", "Elf");

    // same assertions with Functions:
    assertThat(fellowshipOfTheRing).flatExtracting(TolkienCharacter::getName,
                                                   tc -> tc.getRace().getName())
                                   .contains("Frodo", "Hobbit", "Legolas", "Elf");
    // end::flat_extracting_multiple_values[]
  }

  @Test
  void element_comparator() {
    // tag::element_comparator[]
    List<TolkienCharacter> fellowshipOfTheRing = list(frodo, sam, merry, pippin, gandalf, legolas, boromir, aragorn, gimli);

    // the fellowshipOfTheRing includes Gandalf but not Sauron ...
    assertThat(fellowshipOfTheRing).contains(gandalf)
                                   .doesNotContain(sauron);

    // ... but if we compare only races, Sauron is in fellowshipOfTheRing since he's a Maia like Gandalf
    assertThat(fellowshipOfTheRing).usingElementComparator((t1, t2) -> t1.getRace().compareTo(t2.getRace()))
                                   .contains(sauron);
    // end::element_comparator[]
  }

  interface TolkienDao {
    List<TolkienCharacter> findHeroes();
  }

  static class Player {

    private final int pointsPerGame;
    private final int assistsPerGame;
    private final int reboundsPerGame;
    private final List<Player> teamMates;

    Player(int pointsPerGame, int assistsPerGame, int reboundsPerGame, Player... teamMates) {
      this.pointsPerGame = pointsPerGame;
      this.assistsPerGame = assistsPerGame;
      this.reboundsPerGame = reboundsPerGame;
      this.teamMates = list(teamMates);
    }

    int pointsPerGame() {
      return pointsPerGame;
    }

    int assistsPerGame() {
      return assistsPerGame;
    }

    int reboundsPerGame() {
      return reboundsPerGame;
    }

    public List<Player> getTeamMates() {
      return teamMates;
    }
  }
}
// @formatter:on
