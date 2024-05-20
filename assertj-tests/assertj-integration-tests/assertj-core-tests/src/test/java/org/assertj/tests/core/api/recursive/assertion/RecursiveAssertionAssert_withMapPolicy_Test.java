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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_VALUES_ONLY;

import org.assertj.core.api.RecursiveAssertionAssert;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.junit.jupiter.api.Test;

class RecursiveAssertionAssert_withMapPolicy_Test {

  @Test
  void should_use_given_MapAssertionPolicy() {
    // GIVEN
    Object object = "foo";
    RecursiveAssertionConfiguration.MapAssertionPolicy mapAssertionPolicy = MAP_OBJECT_ONLY;
    // WHEN
    RecursiveAssertionAssert recursiveAssertionAssert = assertThat(object).usingRecursiveAssertion()
                                                                          .withMapAssertionPolicy(mapAssertionPolicy);
    // THEN
    RecursiveAssertionConfiguration expectedConfig = RecursiveAssertionConfiguration.builder()
                                                                                    .withMapAssertionPolicy(mapAssertionPolicy)
                                                                                    .build();
    then(recursiveAssertionAssert).hasFieldOrPropertyWithValue("recursiveAssertionConfiguration", expectedConfig);
  }

  @Test
  void should_use_given_MAP_VALUES_ONLY_MapAssertionPolicy_by_default() {
    // GIVEN
    Object object = "foo";
    // WHEN
    RecursiveAssertionAssert recursiveAssertionAssert = assertThat(object).usingRecursiveAssertion();
    // THEN
    RecursiveAssertionConfiguration expectedConfig = RecursiveAssertionConfiguration.builder()
                                                                                    .withMapAssertionPolicy(MAP_VALUES_ONLY)
                                                                                    .build();
    then(recursiveAssertionAssert).hasFieldOrPropertyWithValue("recursiveAssertionConfiguration", expectedConfig);
  }

}
