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
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ShortArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrayAssert#usingDefaultComparator()}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ShortArrayAssert_usingDefaultComparator_Test {

  @Test
  public void should_revert_to_default_comparator() {
    // GIVEN
    Comparator<short[]> comparator = alwaysEqual();
    ShortArrayAssert assertions = new ShortArrayAssert(emptyArray());
    ShortArrays defaultShortArrays = EXTRACTION.fieldValue("arrays", ShortArrays.class, assertions);
    // WHEN
    assertions.usingComparator(comparator)
              .usingDefaultComparator();
    // THEN
    Objects objects = EXTRACTION.fieldValue("objects", Objects.class, assertions);
    then(objects).isSameAs(Objects.instance());
    ShortArrays shortArrays = EXTRACTION.fieldValue("arrays", ShortArrays.class, assertions);
    then(shortArrays).isSameAs(defaultShortArrays);
  }
}
