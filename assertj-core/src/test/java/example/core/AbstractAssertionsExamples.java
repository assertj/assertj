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
// tag::static_imports[]
import static org.assertj.core.api.Assertions.assertThat;  // main one
import static org.assertj.core.api.Assertions.atIndex; // for List assertions
import static org.assertj.core.api.Assertions.entry;  // for Map assertions
import static org.assertj.core.api.Assertions.tuple; // when extracting several properties at once
import static org.assertj.core.api.Assertions.fail; // use when writing exception tests
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown; // idem
import static org.assertj.core.api.Assertions.filter; // for Iterable/Array assertions
import static org.assertj.core.api.Assertions.offset; // for floating number assertions
import static org.assertj.core.api.Assertions.anyOf; // use with Condition
import static org.assertj.core.api.Assertions.contentOf; // use with File assertions
// end::static_imports[]
import static org.assertj.core.util.Lists.list;

import java.util.List;

import example.data.TolkienCharacter;

// data shared by the documentation examples, the static imports above are only there to be shown in the documentation
public abstract class AbstractAssertionsExamples {

  protected final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
  protected final TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
  protected final TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
  protected final TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
  protected final TolkienCharacter gandalf = new TolkienCharacter("Gandalf", 2020, MAIA);
  protected final TolkienCharacter legolas = new TolkienCharacter("Legolas", 1000, ELF);
  protected final TolkienCharacter boromir = new TolkienCharacter("Boromir", 37, MAN);
  protected final TolkienCharacter aragorn = new TolkienCharacter("Aragorn", 87, MAN);
  protected final TolkienCharacter gimli = new TolkienCharacter("Gimli", 139, DWARF);
  protected final TolkienCharacter sauron = new TolkienCharacter("Sauron", 50000, MAIA);

  protected final List<TolkienCharacter> fellowshipOfTheRing = list(frodo, sam, merry, pippin, gandalf, legolas, boromir,
                                                                    aragorn, gimli);
}
// @formatter:on
