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
package org.assertj.core.internal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Joel Costigliola
 */
class StandardComparisonStrategy_iterableContains_Test {

  private final StandardComparisonStrategy underTest = StandardComparisonStrategy.instance();

  @ParameterizedTest
  @CsvSource({
      "Frodo, true",
      " , true",
      "Sauron, false"
  })
  void should_pass(String value, boolean expected) {
    // GIVEN
    List<?> list = newArrayList("Sam", "Merry", null, "Frodo");
    // WHEN
    boolean result = underTest.iterableContains(list, value);
    // THEN
    then(result).isEqualTo(expected);
  }

  @Test
  void should_return_false_if_iterable_is_null() {
    // WHEN
    boolean result = underTest.iterableContains(null, "Sauron");
    // THEN
    then(result).isFalse();
  }

}
