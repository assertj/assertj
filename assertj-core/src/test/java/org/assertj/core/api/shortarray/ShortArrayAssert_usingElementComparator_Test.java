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
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.testkit.ShortArrays.emptyArray;
import static org.assertj.core.util.introspection.FieldSupport.EXTRACTION;

import java.util.Comparator;

import org.assertj.core.api.ShortArrayAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrayAssert#usingElementComparator(Comparator)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ShortArrayAssert_usingElementComparator_Test {

  @Test
  public void should_set_element_comparator() {
    // GIVEN
    Comparator<Short> comparator = alwaysEqual();
    ShortArrayAssert assertions = new ShortArrayAssert(emptyArray());
    // WHEN
    assertions.usingElementComparator(comparator);
    // THEN
    Comparator<?> assertionComparator = EXTRACTION.fieldValue("arrays.arrays.comparisonStrategy.comparator", Comparator.class,
                                                              assertions);
    then(assertionComparator).isSameAs(comparator);
  }
}
