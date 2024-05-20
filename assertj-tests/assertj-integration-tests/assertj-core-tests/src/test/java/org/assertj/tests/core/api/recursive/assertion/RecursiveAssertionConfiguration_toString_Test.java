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
package org.assertj.tests.core.api.recursive.assertion;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.ELEMENTS_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_VALUES_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_VALUE_ONLY;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.Builder;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionIntrospectionStrategy;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveAssertionConfiguration_toString_Test {

  private Builder recursiveAssertionConfigurationBuilder;

  @BeforeEach
  void setup() {
    recursiveAssertionConfigurationBuilder = RecursiveAssertionConfiguration.builder();
  }

  @Test
  void should_show_a_complete_description() {
    // GIVEN
    recursiveAssertionConfigurationBuilder.withIgnorePrimitiveFields(true)
                                          .withIgnoreAllNullFields(true)
                                          .withIgnoredFields("foo", "bar", "foo.bar")
                                          .withIgnoredFieldsMatchingRegexes("f.*", ".ba.", "..b%sr..")
                                          .withIgnoredFieldsOfTypes(UUID.class, ZonedDateTime.class)
                                          .withRecursionIntoJavaClassLibraryTypes(true)
                                          .withCollectionAssertionPolicy(ELEMENTS_ONLY)
                                          .withMapAssertionPolicy(MAP_VALUES_ONLY)
                                          .withOptionalAssertionPolicy(OPTIONAL_VALUE_ONLY)
                                          .withIntrospectionStrategy(new MyIntrospectionStrategy());
    // WHEN
    RecursiveAssertionConfiguration recursiveAssertionConfiguration = recursiveAssertionConfigurationBuilder.build();
    // THEN
    //@format:off
    then(recursiveAssertionConfiguration).hasToString(format("- all null fields were ignored in the assertion%n" +
                                                             "- the following fields were ignored in the comparison: foo, bar, foo.bar%n" +
                                                             "- the fields matching the following regexes were ignored in the comparison: f.*, .ba., ..b%%sr..%n" +
                                                             "- the following types were ignored in the assertion: java.util.UUID, java.time.ZonedDateTime%n" +
                                                             "- primitive fields were ignored in the recursive assertion%n" +
                                                             "- fields from Java Class Library types (java.* or javax.*) were included in the recursive assertion%n" +
                                                             "- the collection assertion policy was ELEMENTS_ONLY%n" +
                                                             "- the map assertion policy was MAP_VALUES_ONLY%n" +
                                                             "- the optional assertion policy was OPTIONAL_VALUE_ONLY%n"+
                                                             "- the introspection strategy used was: not introspecting anything!%n"));
    //@format:on
  }

  @Test
  void should_show_a_complete_description_with_default_values() {
    // WHEN
    RecursiveAssertionConfiguration recursiveAssertionConfiguration = recursiveAssertionConfigurationBuilder.build();
    // THEN
    // @format:off
    then(recursiveAssertionConfiguration).hasToString(format("- fields from Java Class Library types (java.* or javax.*) were excluded in the recursive assertion%n" +
                                                             "- the collection assertion policy was ELEMENTS_ONLY%n" +
                                                             "- the map assertion policy was MAP_VALUES_ONLY%n"+
                                                             "- the optional assertion policy was OPTIONAL_VALUE_ONLY%n"+
                                                             "- the introspection strategy used was: DefaultRecursiveAssertionIntrospectionStrategy which introspects all fields (including inherited ones)%n"));
    // @format:on
  }

  static class MyIntrospectionStrategy implements RecursiveAssertionIntrospectionStrategy {

    @Override
    public List<RecursiveAssertionNode> getChildNodesOf(Object node) {
      return emptyList();
    }

    @Override
    public String getDescription() {
      return "not introspecting anything!";
    }
  }
}
