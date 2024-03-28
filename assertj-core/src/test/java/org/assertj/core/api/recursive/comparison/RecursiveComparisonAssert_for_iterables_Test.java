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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Comparator;
import java.util.Set;

import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_for_iterables_Test {

  private Set<Person> actual;

  @BeforeEach
  void setup() {
    actual = newLinkedHashSet(new Person("Sheldon"), new Person("Leonard"));
  }

  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
  @Test
  void should_be_directly_usable_with_iterables() {
    // GIVEN
    Set<PersonDto> expected = newHashSet(new PersonDto("Sheldon"), new PersonDto("Leonard"));
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  @Test
  void should_propagate_comparator_by_type() {
    // GIVEN
    Comparator<String> alwayEqualsString = ALWAYS_EQUALS_STRING;
    // WHEN
    RecursiveComparisonConfiguration assertion = assertThat(actual).usingComparatorForType(alwayEqualsString, String.class)
                                                                   .usingRecursiveComparison()
                                                                   .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(assertion.comparatorByTypes()).anyMatch(e -> e.getKey().actual() == String.class && e.getKey().expected() == null
                                                            && e.getValue() != null);
  }

}
