/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.recursive.comparison.RecursiveComparator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

// only check we are using the proper comparator since the implementation of isIn only switched comparator before calling
// the super call isIn method.
class RecursiveComparisonAssert_isNotIn_Test {

  @Test
  void should_use_recursive_comparator() {
    // GIVEN
    Person actual = new Person("jack");
    Person other = new Person("john");
    // WHEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    RecursiveComparisonAssert<?> recursiveComparisonAssert = assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                               .isNotIn(other);
    // THEN
    Object comparator = FieldSupport.extraction().fieldValue("objects.comparisonStrategy.comparator", Object.class,
                                                             recursiveComparisonAssert);
    then(comparator).isInstanceOf(RecursiveComparator.class);
    Object configuration = FieldSupport.extraction().fieldValue("recursiveComparisonConfiguration", Object.class, comparator);
    then(configuration).isSameAs(recursiveComparisonConfiguration);
  }

  @Test
  void should_succeed() {
    // GIVEN
    Person actual = new Person("jack");
    Person other1 = new Person("john");
    Person other2 = new Person("jim");
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .isNotIn(other1, other2)
                .isNotIn(list(other1, other2));
  }

}
