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
package org.assertj.tests.core.api.recursive.comparison.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class RecursiveComparisonAssert_isEqualTo_ignoringTransientFields_Test
    extends WithComparingPropertiesIntrospectionStrategyBaseTest {

  @Test
  void should_fail_since_ignoringTransientFields_does_not_make_sense_for_properties() {
    // GIVEN
    var actual = new WithTransientFields("Jack transient", "Jack");
    var expected = new WithTransientFields("John transient", "Jeff");
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                                         .ignoringTransientFields()
                                                                                         .isEqualTo(expected));
    // THEN
    then(illegalArgumentException).hasMessage("ignoringTransientFields is not supported since we are comparing properties");
  }

  @SuppressWarnings("ClassCanBeRecord")
  static class WithTransientFields {
    final transient String transientName;
    final String name;

    WithTransientFields(String transientName, String name) {
      this.transientName = transientName;
      this.name = name;
    }
  }

}
