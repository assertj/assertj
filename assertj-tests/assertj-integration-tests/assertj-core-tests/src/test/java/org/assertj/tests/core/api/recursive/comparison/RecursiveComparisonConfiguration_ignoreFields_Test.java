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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Set;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_ignoreFields_Test {

  @Test
  void should_register_fields_path_to_ignore_without_duplicates() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar", "bar");
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getIgnoredFields();
    // THEN
    then(fields).containsExactlyInAnyOrder("foo", "bar", "foo.bar");
  }

}
