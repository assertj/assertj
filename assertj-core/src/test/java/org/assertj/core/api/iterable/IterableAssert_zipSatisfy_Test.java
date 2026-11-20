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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setMaxStackTraceElementsDisplayed;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.getMaxStackTraceElementsDisplayed;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.BiConsumer;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IterableAssert_zipSatisfy_Test extends IterableAssertBaseTest {

  private BiConsumer<Object, String> requirements;
  private List<String> other;

  @BeforeEach
  void beforeOnce() {
    requirements = (o1, o2) -> assertThat(o1).hasSameHashCodeAs(o2);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.zipSatisfy(other, requirements);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertZipSatisfy(getInfo(assertions), getActual(assertions), other, requirements);
  }

  @Test
  void should_include_stack_traces_in_failure_order() {
    // GIVEN
    var actual = List.of("first%", "second%");
    var expected = List.of("FIRST", "SECOND");
    // WHEN
    var error = expectAssertionError(() -> then(actual).zipSatisfy(expected, (a, e) -> then(a).startsWith(e)));
    // THEN
    then(error.getMessage()).containsSubsequence("error:", "first%", "\tat ", "error:", "second%", "\tat ")
                            .contains("remaining lines not displayed");
    then(error.getMessage().lines().filter(line -> line.startsWith("\tat "))).hasSize(6);
  }

  @Test
  void should_use_custom_representation_for_requirement_errors() {
    // GIVEN
    var representation = new StandardRepresentation() {
      @Override
      protected String toStringOf(Throwable throwable) {
        return "custom failure";
      }
    };
    // WHEN
    var error = expectAssertionError(() -> then(List.of("actual")).withRepresentation(representation)
                                                                  .zipSatisfy(List.of("expected"),
                                                                              (a, e) -> then(a).startsWith(e)));
    // THEN
    then(error).hasMessageContaining("error: custom failure")
               .hasMessageNotContaining("\tat ");
  }

  @Test
  void should_respect_disabled_stack_traces() {
    // GIVEN
    int previousLimit = getMaxStackTraceElementsDisplayed();
    setMaxStackTraceElementsDisplayed(0);
    try {
      // WHEN
      var error = expectAssertionError(() -> then(List.of("actual")).zipSatisfy(List.of("expected"),
                                                                                (a, e) -> then(a).startsWith(e)));
      // THEN
      then(error).hasMessageContaining("to start with:")
                 .hasMessageNotContainingAny("\tat ", "remaining lines not displayed");
    } finally {
      setMaxStackTraceElementsDisplayed(previousLimit);
    }
  }

  @Test
  void should_include_nested_zip_requirement_stack_traces() {
    // GIVEN
    var actual = List.of(List.of("actual"));
    var expected = List.of(List.of("expected"));
    BiConsumer<String, String> nestedRequirements = (a, e) -> then(a).startsWith(e);
    // WHEN
    var error = expectAssertionError(() -> then(actual).zipSatisfy(expected,
                                                                   (a, e) -> then(a).zipSatisfy(e, nestedRequirements)));
    // THEN
    then(error).hasMessageContainingAll("(\"actual\", \"expected\")", "\tat ");
  }
}
