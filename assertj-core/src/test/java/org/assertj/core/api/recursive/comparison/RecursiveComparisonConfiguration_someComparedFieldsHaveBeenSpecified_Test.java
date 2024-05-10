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

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_someComparedFieldsHaveBeenSpecified_Test {

  private final RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();

  @Test
  void should_return_true_if_some_compared_fields_have_been_specified() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields("name");
    // WHEN
    boolean someComparedFieldsHaveBeenSpecified = recursiveComparisonConfiguration.someComparedFieldsHaveBeenSpecified();
    // THEN
    then(someComparedFieldsHaveBeenSpecified).isTrue();
  }

  @Test
  void should_return_false_if_no_compared_fields_have_been_specified() {
    // WHEN
    boolean someComparedFieldsHaveBeenSpecified = recursiveComparisonConfiguration.someComparedFieldsHaveBeenSpecified();
    // THEN
    then(someComparedFieldsHaveBeenSpecified).isFalse();
  }

}
