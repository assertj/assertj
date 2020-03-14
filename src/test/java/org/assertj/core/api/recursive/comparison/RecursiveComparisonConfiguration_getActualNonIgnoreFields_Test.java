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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.Date;
import java.util.Set;

import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RecursiveComparisonConfiguration getNonIgnoredActualFieldNames")
public class RecursiveComparisonConfiguration_getActualNonIgnoreFields_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_compute_ignored_fields() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*umber");
    recursiveComparisonConfiguration.ignoreFields("people.name");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(Date.class);
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    person1.dateOfBirth = new Date(123);
    person1.neighbour = new Person("Jack");
    person1.neighbour.home.address.number = 123;
    Person person2 = new Person("John");
    person2.home.address.number = 1;
    person2.dateOfBirth = new Date(123);
    person2.neighbour = new Person("Jim");
    person2.neighbour.home.address.number = 456;
    DualValue dualValue = new DualValue(list("people"), person1, person2);
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getNonIgnoredActualFieldNames(dualValue);
    // THEN
    assertThat(fields).doesNotContain("number", "dateOfBirth", "name");
  }

}
