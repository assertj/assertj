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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.ObjectArrayElementComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.test.Name;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_usingRecursiveFieldByFieldElementComparatorOnFields_Test extends ObjectArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @BeforeEach
  void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparatorOnFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    then(arraysBefore).isNotSameAs(getArrays(assertions));
    then(getArrays(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    then(getObjects(assertions).getComparisonStrategy()).isInstanceOf(ObjectArrayElementComparisonStrategy.class);
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
                                                                                                        .withComparedFields("field")
                                                                                                        .build();
    ConfigurableRecursiveFieldByFieldComparator expectedComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration);
    then(getArrays(assertions).getComparator()).isEqualTo(expectedComparator);
    then(getObjects(assertions).getComparisonStrategy()).extracting("elementComparator").isEqualTo(expectedComparator);
  }

  @Test
  void should_compare_given_fields_recursively_and_none_other() {
    // GIVEN
    Player rose = new Player(new Name("Derrick", "Rose"), "Chicago Bulls");
    rose.nickname = new Name("Crazy", "Dunks");
    Player jalen = new Player(new Name("Jalen", "Rose"), "Chicago Bulls");
    jalen.nickname = new Name("Crazy", "Defense");
    // WHEN/THEN
    then(array(rose)).usingRecursiveFieldByFieldElementComparatorOnFields("name.last", "team", "nickname.first")
                     .contains(jalen);
  }

}
