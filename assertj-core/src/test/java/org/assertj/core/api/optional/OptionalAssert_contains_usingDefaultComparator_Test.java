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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Comparator;
import java.util.Optional;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link OptionalAssert#usingDefaultComparator()}</code>.
 *
 * @author Sára Juhošová
 */
class OptionalAssert_contains_usingDefaultComparator_Test {

  private final Comparator<String> STRING_COMPARATOR = Comparator.comparing(String::toLowerCase);

  @Test
  void should_succeed_if_default_equal_content() {
    // GIVEN
    OptionalAssert<String> optionalAssert = assertThat(Optional.of("hello"));
    String expected = "hello";
    // set to different strategy
    optionalAssert.usingValueComparator(STRING_COMPARATOR).contains(expected);
    // WHEN/THEN
    // go back to default strategy
    optionalAssert.usingDefaultValueComparator().contains(expected);
  }

  @Test
  void should_fail_if_different_capitalisation() {
    // GIVEN
    Optional<String> actual = Optional.of("hello");
    String expected = "HellO";
    OptionalAssert<String> optionalAssert = assertThat(actual);
    optionalAssert.usingValueComparator(STRING_COMPARATOR).contains(expected);
    // WHEN
    // go back to default strategy
    AssertionError assertionError = expectAssertionError(() -> optionalAssert.usingDefaultValueComparator().contains(expected));
    // THEN
    then(assertionError).hasMessageContainingAll(actual.toString(), expected);
  }
}
