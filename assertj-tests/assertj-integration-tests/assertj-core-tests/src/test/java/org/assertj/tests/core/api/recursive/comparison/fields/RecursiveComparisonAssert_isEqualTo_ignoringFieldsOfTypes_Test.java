/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison.fields;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.assertj.tests.core.api.recursive.data.WithObject;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class RecursiveComparisonAssert_isEqualTo_ignoringFieldsOfTypes_Test
    extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @Test
  void should_ignore_elements_of_ignored_type_in_collections() {
    // GIVEN
    var actual = WithObject.of(List.of("a", "b"));
    var expected = WithObject.of(List.of("a", "c"));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .ignoringFieldsOfTypes(String.class)
                .isEqualTo(expected);
  }


}
