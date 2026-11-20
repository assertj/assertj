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
package example.guava;

// @formatter:off

import static com.google.common.collect.Lists.newArrayList;
// tag::imports[]
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.entry;
// end::imports[]

import org.junit.jupiter.api.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.Table;

// not run by the build (class name does not match *Test)
class GuavaAssertionsExample {

  @Test
  void multimap() {
    // tag::multimap[]
    // Multimap assertions
    Multimap<String, String> actual = ArrayListMultimap.create();
    actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

    assertThat(actual).containsKeys("Lakers", "Spurs");
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
    // end::multimap[]
  }

  @Test
  void multiset() {
    // tag::multiset[]
    // Multiset assertions
    Multiset<String> actual = HashMultiset.create();
    actual.add("shoes", 2);

    assertThat(actual).contains(2, "shoes");
    assertThat(actual).containsAtLeast(1, "shoes");
    assertThat(actual).containsAtMost(3, "shoes");
    // end::multiset[]
  }

  @Test
  void range() {
    // tag::range[]
    // Range assertions
    Range<Integer> range = Range.closed(10, 12);

    assertThat(range).isNotEmpty()
                     .contains(10, 11, 12)
                     .hasClosedLowerBound()
                     .hasLowerEndpointEqualTo(10)
                     .hasUpperEndpointEqualTo(12);
    // end::range[]
  }

  @Test
  void table() {
    // tag::table[]
    // Table assertions
    Table<Integer, String, String> bestMovies = HashBasedTable.create();

    bestMovies.put(1970, "Palme d'Or", "M.A.S.H");
    bestMovies.put(1994, "Palme d'Or", "Pulp Fiction");
    bestMovies.put(2008, "Palme d'Or", "Entre les murs");
    bestMovies.put(2000, "Best picture Oscar", "American Beauty");
    bestMovies.put(2011, "Goldener Bär", "A Separation");

    assertThat(bestMovies).hasRowCount(5).hasColumnCount(3).hasSize(5)
                          .containsValues("American Beauty", "A Separation", "Pulp Fiction")
                          .containsCell(1994, "Palme d'Or", "Pulp Fiction")
                          .containsColumns("Palme d'Or", "Best picture Oscar", "Goldener Bär")
                          .containsRows(1970, 1994, 2000, 2008, 2011);
    // end::table[]
  }
}
// @formatter:on
