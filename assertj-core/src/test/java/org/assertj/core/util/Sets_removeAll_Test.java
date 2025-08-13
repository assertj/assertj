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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.removeAll;

import java.util.Set;

import org.junit.jupiter.api.Test;

class Sets_removeAll_Test {
  @Test
  void should_return_a_copy_of_the_reference_sets_without_the_elements_of_the_other_set() {
    // GIVEN
    var referenceSet = newLinkedHashSet("1", "2", "3");
    var otherSet = newLinkedHashSet("1", "3");
    // WHEN
    Set<String> result = removeAll(referenceSet, otherSet);
    // THEN
    then(result).containsExactly("2");
    then(referenceSet).containsExactly("1", "2", "3");
  }
}
