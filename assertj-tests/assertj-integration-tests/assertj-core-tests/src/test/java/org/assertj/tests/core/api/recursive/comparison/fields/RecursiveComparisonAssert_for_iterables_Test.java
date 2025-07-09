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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.DualClass.dualClass;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;

import java.util.Set;

import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_for_iterables_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  private Set<Person> actual;

  @BeforeEach
  public void beforeEachTest() {
    super.beforeEachTest();
    actual = newLinkedHashSet(new Person("Sheldon"), new Person("Leonard"));
  }

  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
  @Test
  void should_be_directly_usable_with_iterables() {
    // GIVEN
    Set<PersonDto> expected = newHashSet(new PersonDto("Sheldon"), new PersonDto("Leonard"));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(expected);
  }

  @Test
  void should_propagate_comparator_by_type() {
    // WHEN
    var configuration = assertThat(actual).usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                          .usingRecursiveComparison(recursiveComparisonConfiguration)
                                          .getRecursiveComparisonConfiguration();
    // THEN
    then(configuration.comparatorByTypes()).contains(entry(dualClass(String.class, null), ALWAYS_EQUALS_STRING));
  }

}
