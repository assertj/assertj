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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_AND_ENTRIES;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_VALUES_ONLY;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_MapPolicyTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  void should_assert_over_map_but_not_keys_or_values_when_policy_is_map_object_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withMapAssertionPolicy(MAP_OBJECT_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("map"));
  }

  @Test
  void should_assert_over_values_but_not_keys_or_map_object_when_policy_is_values_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withMapAssertionPolicy(MAP_VALUES_ONLY)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("map").field("VAL[value0]"),
                                    rootFieldLocation().field("map").field("VAL[value1]"));
  }

  @Test
  void should_assert_over_map_object_and_keys_and_values_when_policy_is_map_object_and_entries() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
                                                                                   .withMapAssertionPolicy(MAP_OBJECT_AND_ENTRIES)
                                                                                   .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).containsOnly(rootFieldLocation().field("map"),
                                    rootFieldLocation().field("map").field("KEY[key0]"),
                                    rootFieldLocation().field("map").field("VAL[value0]"),
                                    rootFieldLocation().field("map").field("KEY[key1]"),
                                    rootFieldLocation().field("map").field("VAL[value1]"));
  }

  private Object classWithMap() {
    return new ClassWithMap();
  }

  class ClassWithMap {
    @SuppressWarnings("unused")
    private Map<String, String> map = Map.of("key0", "value0", "key1", "value1");
  }
}
